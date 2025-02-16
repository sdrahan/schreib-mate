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
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
public class SochinenieBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SochinenieBot.class);

    private final TelegramClient telegramClient;
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
        this.telegramClient = new OkHttpTelegramClient(getBotToken());
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
        if (update.hasMessage()) {
            if (update.getMessage().hasPhoto()) {
                handlePhotoMessage(update.getMessage());
            } else if (update.getMessage().hasText()) {
                handleTextMessage(update.getMessage());
            }
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private void handlePhotoMessage(Message message) {
        long chatId = message.getChatId();
        try {
            // Get the largest resolution photo
            List<PhotoSize> photos = message.getPhoto();
            PhotoSize largestPhoto = photos.stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);
            String filePath = getFilePath(largestPhoto);
            java.io.File imageFile = downloadPhotoByFilePath(filePath);

            // Extract text from the image
            String extractedText = chatGPTService.extractTextFromImage(imageFile);
            if (extractedText.isEmpty()) {
                sendMessage(chatId, "Couldn't extract text from the image. Please try again.");
                return;
            }

            // Retrieve user and current assignment
            Long telegramUserId = message.getFrom().getId();
            String telegramUsername = message.getFrom().getUserName();
            String language = message.getFrom().getLanguageCode();
            User user = userService.getOrCreateUser(telegramUserId, telegramUsername, chatId, language);
            Assignment currentAssignment = assignmentService.getCurrentActiveAssignment(user);
            String topic = currentAssignment.getTopic().getTopicDe();

            // Validate the extracted text against the topic
            boolean isRelated = chatGPTService.validateSubmission(extractedText, topic);
            if (!isRelated) {
                sendMessage(chatId, "Your submission doesn't seem to be related to the assignment topic: *"
                        + topic + "*.\nPlease try again with an essay on this topic.");
                return;
            }

            // If validation passes, generate detailed grammatical feedback
            String feedback = chatGPTService.getFeedback(extractedText);
            sendMessage(chatId, "Here's the extracted text and feedback:\n" + feedback);
        } catch (Exception e) {
            LOGGER.error("Error processing image", e);
            sendMessage(chatId, "Failed to process the image. Please try again.");
        }
    }

    public java.io.File downloadPhotoByFilePath(String filePath) {
        try {
            return telegramClient.downloadFile(filePath);
        } catch (TelegramApiException e) {
            LOGGER.error("Error downloading the file from telegram", e);
        }

        return null;
    }

    public String getFilePath(PhotoSize photo) {
        Objects.requireNonNull(photo);

        if (photo.getFilePath() != null) {
            return photo.getFilePath();
        } else {
            GetFile getFileMethod = new GetFile(photo.getFileId());
            try {
                File file = telegramClient.execute(getFileMethod);
                return file.getFilePath();
            } catch (TelegramApiException e) {
                LOGGER.error("Error getting photo's file path", e);
            }
        }

        return null;
    }

    private void handleTextMessage(Message incomingMessage) {
        long chatId = incomingMessage.getChatId();
        String incomingMessageText = incomingMessage.getText().trim();
        Long telegramUserId = incomingMessage.getFrom().getId();
        String telegramUsername = incomingMessage.getFrom().getUserName();
        String language = incomingMessage.getFrom().getLanguageCode();

        boolean userExists = userService.userExists(telegramUserId);
        User user = userService.getOrCreateUser(telegramUserId, telegramUsername, chatId, language);

        if (!userExists || incomingMessageText.equalsIgnoreCase("/start")) {
            sendMessage(chatId, "Hi! I'm now going to give you your first assignment!");
            Assignment firstAssignment = assignmentService.assignNewTopic(user);
            sendAssignment(chatId, firstAssignment);
            return;
        }

        Assignment currentAssignment = assignmentService.getCurrentActiveAssignment(user);
        String topic = currentAssignment.getTopic().getTopicDe();

        // First, check if the submission is really related to the assignment topic.
        boolean isRelated = chatGPTService.validateSubmission(incomingMessageText, topic);
        if (!isRelated) {
            sendMessage(chatId, "Your submission doesn't seem to be related to the assignment topic: *"
                    + topic + "*.\nPlease try again with an essay on this topic.");
            return;
        }

        // If valid, mark as submitted and proceed.
        assignmentService.changeAssignmentState(currentAssignment, AssignmentState.SUBMITTED);
        removeInlineKeyboard(currentAssignment.getTelegramMessageId(), chatId);

        String feedback = chatGPTService.getFeedback(incomingMessageText);
        Message sentMessage = sendMessageWithButton(chatId, feedback + "\n\nWould you like a new assignment?",
                "I'm done, give me another", "new_assignment");
        assignmentService.setTelegramMessageId(currentAssignment, sentMessage.getMessageId());
    }


    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId(); // Message ID of the button message
        Long telegramUserId = callbackQuery.getFrom().getId();
        String telegramUsername = callbackQuery.getFrom().getUserName();
        String language = callbackQuery.getFrom().getLanguageCode();
        User user = userService.getOrCreateUser(telegramUserId, telegramUsername, chatId, language);

        // Remove the button after pressing
        removeInlineKeyboard(messageId, chatId);

        if (callbackData.equals("new_assignment")) {
            assignNewAssignment(chatId, user);
        }
    }

    private void assignNewAssignment(Long chatId, User user) {
        Assignment currentAssignment = assignmentService.getCurrentActiveAssignment(user);

        if (currentAssignment.getState() == AssignmentState.SUBMITTED) {
            sendMessage(chatId, "Ok, here's your new assignment.");
            assignmentService.changeAssignmentState(currentAssignment, AssignmentState.DONE);
        } else {
            sendMessage(chatId, "I understand you don't want this one. Let's assign you another.");
            assignmentService.changeAssignmentState(currentAssignment, AssignmentState.CANCELLED);
        }

        Assignment newAssignment = assignmentService.assignNewTopic(user);
        sendAssignment(chatId, newAssignment);
    }

    private void sendAssignment(Long chatId, Assignment assignment) {
        String assignmentText = String.format("📌 *Your Assignment:*\n*%s*\n\n%s\n\n*Keywords:*\n%s",
                assignment.getTopic().getTopicDe(),
                assignment.getTopic().getDescriptionDe(),
                assignment.getTopic().getKeywordsDe());

        Message message = sendMessageWithButton(chatId, assignmentText, "I want another one", "new_assignment");
        assignmentService.setTelegramMessageId(assignment, message.getMessageId());
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("Markdown")
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Error sending message: {}", e.getMessage(), e);
        }
    }

    private Message sendMessageWithButton(Long chatId, String text, String buttonText, String callbackData) {
        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(buttonText)
                        .callbackData(callbackData)
                        .build())))
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("Markdown")
                .replyMarkup(keyboard)
                .build();

        try {
            return telegramClient.execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Error sending message: {}", e.getMessage(), e);
        }
        return null;
    }

    private void removeInlineKeyboard(int messageId, long chatId) {
        EditMessageReplyMarkup editMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(new InlineKeyboardMarkup(Collections.emptyList())) // Empty keyboard to remove buttons
                .build();
        try {
            telegramClient.execute(editMarkup);
        } catch (TelegramApiException e) {
            LOGGER.error("Error removing inline keyboard: {}", e.getMessage(), e);
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        LOGGER.info("Registered bot running state is: {}", botSession.isRunning());
    }
}
