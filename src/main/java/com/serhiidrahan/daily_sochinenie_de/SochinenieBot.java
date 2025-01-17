package com.serhiidrahan.daily_sochinenie_de;

import com.serhiidrahan.daily_sochinenie_de.entity.User;
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
    private final ChatGPTService chatGPTService;
    private final String botToken;

    public SochinenieBot(UserService userService, ChatGPTService chatGPTService, @Value("${telegrambot.token}") String botToken) {
        this.userService = userService;
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
            final User user = userService.getUser(incomingMessage.getFrom().getId(), incomingMessage.getFrom().getUserName(), chatId);
            String incomingMessageText = incomingMessage.getText();

            // Get feedback from ChatGPT
            String feedback = chatGPTService.getFeedback(incomingMessageText);

            SendMessage message = SendMessage // Create a message object
                    .builder()
                    .chatId(chatId)
                    .text(feedback)
                    .build();
            try {
                telegramClient.execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        System.out.println("Registered bot running state is: " + botSession.isRunning());
    }
}
