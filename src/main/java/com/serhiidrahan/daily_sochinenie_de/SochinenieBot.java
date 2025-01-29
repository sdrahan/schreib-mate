package com.serhiidrahan.daily_sochinenie_de;

import com.serhiidrahan.daily_sochinenie_de.entity.Assignment;
import com.serhiidrahan.daily_sochinenie_de.entity.User;
import com.serhiidrahan.daily_sochinenie_de.enums.AssignmentState;
import com.serhiidrahan.daily_sochinenie_de.service.AssignmentService;
import com.serhiidrahan.daily_sochinenie_de.service.ChatGPTService;
import com.serhiidrahan.daily_sochinenie_de.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class SochinenieBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SochinenieBot.class);

    private TelegramClient telegramClient;
    private final UserService userService;
    private final AssignmentService assignmentService;
    private final ChatGPTService chatGPTService;
    private final String botToken;

    public SochinenieBot(UserService userService, AssignmentService assignmentService, ChatGPTService chatGPTService,
                         @Value("${telegrambot.token}") String botToken) {
        this.userService = userService;
        this.assignmentService = assignmentService;
        this.chatGPTService = chatGPTService;
        this.botToken = botToken;
        telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message incomingMessage = update.getMessage();
            long chatId = incomingMessage.getChatId();
            String incomingMessageText = incomingMessage.getText().trim();
            Long telegramUserId = incomingMessage.getFrom().getId();
            String telegramUsername = incomingMessage.getFrom().getUserName();

            // Get user from database (or create if new)

            boolean userExists = userService.userExists(telegramUserId);
            User user = userService.getUser(telegramUserId, telegramUsername, chatId);

            // Handle first-time user (`/start` command)
            if (!userExists || incomingMessageText.equalsIgnoreCase("/start")) {
                sendMessage(chatId, "Hi! I'm now going to give you your first assignment!");
                Assignment firstAssignment = assignmentService.assignNewTopic(user);
                sendAssignment(chatId, firstAssignment);
                return;
            }

            // Handle "new" command
            if (incomingMessageText.equalsIgnoreCase("new")) {
                Assignment currentAssignment = assignmentService.getCurrentActiveAssignment(user);

                if (currentAssignment.getState() == AssignmentState.SUBMITTED) {
                    sendMessage(chatId, "Ok, here's your new assignment.");
                    assignmentService.changeAssignmentState(currentAssignment, AssignmentState.DONE);
                } else {
                    sendMessage(chatId, "I understand you don't want this one. Let's assign you another.");
                    assignmentService.changeAssignmentState(currentAssignment, AssignmentState.CANCELLED);
                }

                // Assign a new assignment
                Assignment newAssignment = assignmentService.assignNewTopic(user);
                sendAssignment(chatId, newAssignment);
                return;
            }

            // Handle normal text submission (mark as submitted & give feedback)
            Assignment currentAssignment = assignmentService.getCurrentActiveAssignment(user);
            assignmentService.changeAssignmentState(currentAssignment, AssignmentState.SUBMITTED);

            // Get ChatGPT feedback
            String feedback = chatGPTService.getFeedback(incomingMessageText);

            // Send feedback and ask if user wants a new assignment
            sendMessage(chatId, feedback + "\n\nAre you ready for the next assignment? Reply \"new\" if yes, or just submit a new response if you wish to retry.");
        }
    }

    private void sendAssignment(Long chatId, Assignment assignment) {
        String assignmentText = String.format("📌 *Your Assignment:*\n*%s*\n\n%s",
                assignment.getTopic().getTopicDe(),
                assignment.getTopic().getDescriptionDe());
        sendMessage(chatId, assignmentText);
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("Markdown") // Allow bold formatting
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Error sending message: {}", e.getMessage(), e);
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        System.out.println("Registered bot running state is: " + botSession.isRunning());
    }
}
