package com.serhiidrahan.daily_sochinenie_de;

import com.serhiidrahan.daily_sochinenie_de.entity.Assignment;
import com.serhiidrahan.daily_sochinenie_de.entity.User;
import com.serhiidrahan.daily_sochinenie_de.enums.AssignmentState;
import com.serhiidrahan.daily_sochinenie_de.enums.Language;
import com.serhiidrahan.daily_sochinenie_de.enums.ValidationError;
import com.serhiidrahan.daily_sochinenie_de.exception.ChatGPTException;
import com.serhiidrahan.daily_sochinenie_de.service.AssignmentService;
import com.serhiidrahan.daily_sochinenie_de.service.ChatGPTService;
import com.serhiidrahan.daily_sochinenie_de.service.LocalizedMessagesService;
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
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SochinenieBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SochinenieBot.class);
    private static final int MIN_SUBMISSION_LENGTH = 10;
    private static final int MAX_SUBMISSION_LENGTH = 4000;
    private final TelegramClient telegramClient;
    private final UserService userService;
    private final AssignmentService assignmentService;
    private final ChatGPTService chatGPTService;
    private final LocalizedMessagesService localizedMessagesService;
    private final String botToken;

    private final ConcurrentHashMap<Long, Boolean> usersExpectingResponse = new ConcurrentHashMap<>();

    public SochinenieBot(UserService userService, AssignmentService assignmentService, ChatGPTService chatGPTService,
                         LocalizedMessagesService localizedMessagesService, @Value("${telegrambot.token}") String botToken) {
        this.userService = userService;
        this.assignmentService = assignmentService;
        this.chatGPTService = chatGPTService;
        this.localizedMessagesService = localizedMessagesService;
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
            long userId = update.getMessage().getFrom().getId();

            // Prevent multiple simultaneous requests from the same user
            if (isUserRequestProcessing(userId)) {
                LOGGER.warn("Received message from user {} before the previous one got processed", userId);
                return;
            }
            markUserAsProcessing(userId);

            try {
                if (update.getMessage().hasPhoto()) {
                    handlePhotoMessage(update.getMessage());
                } else if (update.getMessage().hasText()) {
                    handleTextMessage(update.getMessage());
                }
            } finally {
                clearUserProcessingStatus(userId);
            }
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private void handlePhotoMessage(Message message) {
        long chatId = message.getChatId();
        Long telegramUserId = message.getFrom().getId();
        String telegramUsername = message.getFrom().getUserName();
        User user = userService.getOrCreateUser(telegramUserId, telegramUsername, chatId);

        try {
            // Extract text from image
            java.io.File imageFile = downloadUserImage(message);
            String extractedText = chatGPTService.extractTextFromImage(imageFile);

            if (extractedText.isEmpty()) {
                LOGGER.warn("Tried extracting text from photo of user {}, but it's empty", telegramUserId);
                sendMessage(chatId, localizedMessagesService.emptyImage(user.getLanguage()));
                return;
            }

            // Process submission as image
            processSubmission(extractedText, user, chatId, true);

        } catch (Exception e) {
            LOGGER.error("Error processing image submission for user {}", telegramUserId, e);
            sendMessage(chatId, localizedMessagesService.errorProcessingImage(user.getLanguage()));
        }
    }

    private void handleTextMessage(Message incomingMessage) {
        long chatId = incomingMessage.getChatId();
        String incomingMessageText = incomingMessage.getText().trim();
        Long telegramUserId = incomingMessage.getFrom().getId();
        String telegramUsername = incomingMessage.getFrom().getUserName();

        boolean userExists = userService.userExists(telegramUserId);
        User user = userService.getOrCreateUser(telegramUserId, telegramUsername, chatId);

        if (incomingMessageText.equalsIgnoreCase("/language")) {
            showLanguageSelection(chatId);
            return;
        }

        if (!userExists || incomingMessageText.equalsIgnoreCase("/start")) {
            showLanguageSelection(chatId);
            return;
        }

        // Process text-based submission
        processSubmission(incomingMessageText, user, chatId, false);
    }


    private void processSubmission(String submission, User user, long chatId, boolean isImageSubmission) {
        Long telegramUserId = user.getTelegramId();
        Language language = user.getLanguage();
        Assignment currentAssignment = assignmentService.getCurrentActiveAssignment(user);
        String topic = currentAssignment.getTopic().getTopicDe();

        try {
            // Validate submission
            ValidationError validationError = validateSubmission(submission, topic);
            if (validationError != null) {
                logValidationError(telegramUserId, user, chatId, topic, submission, validationError);
                sendMessage(chatId, getValidationErrorMessage(validationError, language, topic));
                return;
            }

            // If valid, mark as submitted and remove keyboard
            assignmentService.changeAssignmentState(currentAssignment, AssignmentState.SUBMITTED);
            removeInlineKeyboard(currentAssignment.getTelegramMessageId(), chatId);

            // Get feedback from OpenAI
            String feedback = chatGPTService.getFeedback(submission, language);
            String responseMessage = (isImageSubmission ? "Here's the extracted text and feedback:\n" : "") + feedback;

            // Send message with "New Assignment" button
            Message sentMessage = sendMessageWithButton(chatId, responseMessage, "I'm done, give me another", "new_assignment");
            assignmentService.setTelegramMessageId(currentAssignment, sentMessage.getMessageId());

        } catch (ChatGPTException e) {
            LOGGER.error("Error processing submission for user {}", telegramUserId, e);
            sendMessage(chatId, localizedMessagesService.errorProcessingImage(language));
        }
    }

    private ValidationError validateSubmission(String submission, String topic) throws ChatGPTException {
        boolean isTooShort = submission.length() < MIN_SUBMISSION_LENGTH;
        if (isTooShort) {
            return ValidationError.TOO_SHORT;
        }
        boolean isTooLong = submission.length() > MAX_SUBMISSION_LENGTH;
        if (isTooLong) {
            return ValidationError.TOO_LONG;
        }
        boolean isRelated = chatGPTService.validateSubmission(submission, topic);
        if (!isRelated) {
            return ValidationError.UNRELATED;
        }
        return null;
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();
        Long telegramUserId = callbackQuery.getFrom().getId();
        String telegramUsername = callbackQuery.getFrom().getUserName();

        clearUserProcessingStatus(telegramUserId);

        // If the callback is for setting the language:
        if (callbackData.startsWith("set_language_")) {
            String langCode = callbackData.substring("set_language_".length());
            Language selectedLanguage = Language.valueOf(langCode);
            // Update the user's language preference.
            User user = userService.getOrCreateUser(telegramUserId, telegramUsername, chatId);
            user.setLanguage(selectedLanguage);
            userService.save(user);

            // Remove the language selection keyboard.
            removeInlineKeyboard(messageId, chatId);

            // Send a confirmation message in the chosen language.
            String confirmation = localizedMessagesService.languageConfirmation(selectedLanguage);
            sendMessage(chatId, confirmation);

            if (assignmentService.getCurrentActiveAssignment(user) == null) {
                // seems like it was the first-time setup
                sendMessage(chatId, "Now I'm now going to give you your first assignment!");
                Assignment firstAssignment = assignmentService.assignNewTopic(user);
                sendAssignment(chatId, firstAssignment, user.getLanguage());
            }

            return;
        }

        if (callbackData.equals("new_assignment")) {
            removeInlineKeyboard(messageId, chatId);
            assignNewAssignment(chatId, userService.getOrCreateUser(telegramUserId, telegramUsername, chatId));
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
        sendAssignment(chatId, newAssignment, user.getLanguage());
    }

    private void showLanguageSelection(long chatId) {
        String messageText = localizedMessagesService.languageSelect();
        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(List.of(
                        InlineKeyboardButton.builder().text("🇬🇧 English").callbackData("set_language_EN").build(),
                        InlineKeyboardButton.builder().text("🇷🇺 Русский").callbackData("set_language_RU").build(),
                        InlineKeyboardButton.builder().text("🇩🇪 Deutsch").callbackData("set_language_DE").build()
                )))
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .replyMarkup(keyboard)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Error sending language selection message: {}", e.getMessage(), e);
        }
    }

    private void sendAssignment(Long chatId, Assignment assignment, Language language) {
        String assignmentText = localizedMessagesService.assignmentText(language,
                assignment.getTopic().getTopic(language),
                assignment.getTopic().getDescription(language),
                assignment.getTopic().getKeywords(language));

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

    private void logValidationError(Long userId, User user, long chatId, String topic, String submission, ValidationError error) {
        String userInfo = String.format("UserID: %d, Username: %s, ChatID: %d, Language: %s",
                userId, user.getTelegramUsername(), chatId, user.getLanguage());

        LOGGER.info("Submission validation failed [{}]: User details: {} | Topic: '{}' | Submission: '{}'",
                error, userInfo, topic, submission.length() > 50 ? submission.substring(0, 50) + "..." : submission);
    }

    private String getValidationErrorMessage(ValidationError validationError, Language language, String topic) {
        return switch (validationError) {
            case TOO_SHORT -> localizedMessagesService.sumbissionTooShort(language);
            case TOO_LONG -> localizedMessagesService.sumbissionTooLong(language, MAX_SUBMISSION_LENGTH);
            case UNRELATED -> localizedMessagesService.submissionTopicIsWrong(language, topic);
        };
    }

    private java.io.File downloadUserImage(Message message) throws Exception {
        List<PhotoSize> photos = message.getPhoto();
        PhotoSize largestPhoto = photos.stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .orElseThrow(() -> new Exception("No photo found in message"));

        String filePath = getFilePath(largestPhoto);
        return downloadPhotoByFilePath(filePath);
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

    private boolean isUserRequestProcessing(Long userId) {
        return usersExpectingResponse.containsKey(userId);
    }

    private void markUserAsProcessing(Long userId) {
        usersExpectingResponse.put(userId, true);
    }

    private void clearUserProcessingStatus(Long userId) {
        usersExpectingResponse.remove(userId);
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        LOGGER.info("Registered bot running state is: {}", botSession.isRunning());
    }
}
