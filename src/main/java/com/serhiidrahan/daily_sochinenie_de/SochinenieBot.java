package com.serhiidrahan.daily_sochinenie_de;

import com.serhiidrahan.daily_sochinenie_de.entity.Assignment;
import com.serhiidrahan.daily_sochinenie_de.entity.AssignmentTopic;
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
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SochinenieBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SochinenieBot.class);
    private static final int MIN_SUBMISSION_LENGTH = 30;
    private static final int MAX_SUBMISSION_LENGTH = 4000;
    private static final int TELEGRAM_MESSAGE_LIMIT = 4000;
    private final TelegramClient telegramClient;
    private final UserService userService;
    private final AssignmentService assignmentService;
    private final ChatGPTService chatGPTService;
    private final LocalizedMessagesService localizedMessagesService;
    private final String botToken;

    private final ConcurrentHashMap<Long, Boolean> usersExpectingResponse = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);


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

            // Prevent multiple requests from the same user
            if (isUserRequestProcessing(userId)) {
                LOGGER.warn("Received message from user {} before the previous one got processed", userId);
                return;
            }
            markUserAsProcessing(userId);

            // Process asynchronously
            executorService.submit(() -> {
                try {
                    if (update.getMessage().hasPhoto()) {
                        handlePhotoMessage(update.getMessage());
                    } else if (update.getMessage().hasText()) {
                        handleTextMessage(update.getMessage());
                    }
                } finally {
                    clearUserProcessingStatus(userId);
                }
            });
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }


    private void handlePhotoMessage(Message message) {
        long chatId = message.getChatId();
        Long telegramUserId = message.getFrom().getId();
        String telegramUsername = message.getFrom().getUserName();
        User user = userService.getOrCreateUser(telegramUserId, telegramUsername, chatId);

        executorService.submit(() -> {
            try {
                // Extract text from image
                java.io.File imageFile = downloadUserImage(message);
                String extractedText = chatGPTService.extractTextFromImage(imageFile);

                if (extractedText.isEmpty()) {
                    LOGGER.warn("Tried extracting text from photo of user {}, but it's empty", telegramUserId);
                    sendMessage(chatId, localizedMessagesService.emptyImage(user.getLanguage()));
                    return;
                }

                processSubmission(extractedText, user, chatId, true);
            } catch (Exception e) {
                LOGGER.error("Error processing image submission for user {}", telegramUserId, e);
                sendMessage(chatId, localizedMessagesService.errorProcessingImage(user.getLanguage()));
            }
        });
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

        if (assignmentService.getCurrentActiveAssignment(user) == null) {
            if (!assignmentService.hasAvailableTopics(user)) {
                sendMessage(chatId, localizedMessagesService.errorNoTopicsLeft(user.getLanguage()));
                return;
            }

            assignFirstAssignment(chatId, user);
            return;
        }

        if (incomingMessageText.equalsIgnoreCase("/new_assignment")) {
            LOGGER.info("User {} asked for new assignment", telegramUserId);

            removeInlineKeyboard(assignmentService.getCurrentActiveAssignment(user).getTelegramMessageId(), chatId);
            assignNewAssignment(chatId, userService.getOrCreateUser(telegramUserId, telegramUsername, chatId));
            return;
        }

        // Process text-based submission
        processSubmission(incomingMessageText, user, chatId, false);
    }


    private void processSubmission(String submission, User user, long chatId, boolean isImageSubmission) {
        Long telegramUserId = user.getTelegramId();
        Language language = user.getLanguage();
        Assignment currentAssignment = assignmentService.getCurrentActiveAssignment(user);

        // If the user has no active assignments, check if we can assign a new one
        if (currentAssignment == null) {
            LOGGER.warn("User {} has no active assignment, but sent a submission.", telegramUserId);
            if (!assignmentService.hasAvailableTopics(user)) {
                sendMessage(chatId, localizedMessagesService.errorNoTopicsLeft(language));
                return;
            }

            assignNewAssignment(chatId, user);
            return;
        }
        String topic = currentAssignment.getTopic().getTopicDe();
        LOGGER.info("User {} submitting text for topic: {}", telegramUserId, topic);

        try {
            // Validate submission
            ValidationError validationError = validateSubmission(submission, topic);
            if (validationError != null) {
                logValidationError(telegramUserId, user, chatId, topic, submission, validationError);
                sendMessage(chatId, getValidationErrorMessage(validationError, language, topic));
                return;
            }

            // Mark as submitted and remove inline keyboard
            assignmentService.changeAssignmentState(currentAssignment, AssignmentState.SUBMITTED);
            removeInlineKeyboard(currentAssignment.getTelegramMessageId(), chatId);

            // Fetch feedback asynchronously
            executorService.submit(() -> {
                try {
                    String feedback = chatGPTService.getFeedback(submission, language);
                    Message sentMessage = sendMessageWithButton(chatId, feedback, localizedMessagesService.buttonIAmDone(language), "new_assignment");
                    assignmentService.setTelegramMessageId(currentAssignment, sentMessage.getMessageId());
                } catch (ChatGPTException e) {
                    LOGGER.error("Error fetching feedback for user {}", telegramUserId, e);
                    sendMessage(chatId, localizedMessagesService.errorGettingFeedback(language));
                }
            });

        } catch (Exception e) {
            LOGGER.error("Unexpected error during submission processing for user {}: {}", telegramUserId, e.getMessage(), e);
            sendMessage(chatId, localizedMessagesService.errorGettingFeedback(language));
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
        LOGGER.info("Received callback '{}' from user {}", callbackData, telegramUserId);

        // If the callback is for setting the language:
        if (callbackData.startsWith("set_language_")) {
            String langCode = callbackData.substring("set_language_".length());
            Language selectedLanguage = Language.valueOf(langCode);
            // Update the user's language preference.
            User user = userService.getOrCreateUser(telegramUserId, telegramUsername, chatId);
            user.setLanguage(selectedLanguage);
            userService.save(user);
            LOGGER.info("User {} changed language to {}", telegramUserId, selectedLanguage);

            // Remove the language selection keyboard.
            removeInlineKeyboard(messageId, chatId);

            // Send a confirmation message in the chosen language.
            String confirmation = localizedMessagesService.languageConfirmation(selectedLanguage);
            sendMessage(chatId, confirmation);

            if (assignmentService.getCurrentActiveAssignment(user) == null) {
                assignFirstAssignment(chatId, user);
            }

            return;
        }

        if (callbackData.equals("new_assignment")) {
            LOGGER.info("User {} asked for new assignment", telegramUserId);
            removeInlineKeyboard(messageId, chatId);
            assignNewAssignment(chatId, userService.getOrCreateUser(telegramUserId, telegramUsername, chatId));
        }
    }

    private void assignFirstAssignment(Long chatId, User user) {
        sendMessage(chatId, localizedMessagesService.firstAssignment(user.getLanguage()));
        Assignment firstAssignment = assignmentService.assignNewTopic(user);
        sendAssignment(chatId, firstAssignment, user.getLanguage());
    }

    private void assignNewAssignment(Long chatId, User user) {
        Assignment currentAssignment = assignmentService.getCurrentActiveAssignment(user);

        if (currentAssignment.getState() == AssignmentState.SUBMITTED) {
            sendMessage(chatId, localizedMessagesService.doneWithTopic(user.getLanguage()));
            assignmentService.changeAssignmentState(currentAssignment, AssignmentState.DONE);
        } else {
            sendMessage(chatId, localizedMessagesService.wantAnotherTopic(user.getLanguage()));
            assignmentService.changeAssignmentState(currentAssignment, AssignmentState.CANCELLED);
        }

        Assignment newAssignment;
        try {
            if (!assignmentService.hasAvailableTopics(user)) {
                sendMessage(chatId, localizedMessagesService.errorNoTopicsLeft(user.getLanguage()));
                return;
            }
            newAssignment = assignmentService.assignNewTopic(user);
        } catch (IllegalStateException e) {
            sendMessage(chatId, localizedMessagesService.errorNoTopicsLeft(user.getLanguage()));
            return;
        }
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
        AssignmentTopic topic = assignment.getTopic();
        String topicText;
        String descriptionText;
        if (language.equals(Language.DE)) {
            topicText = topic.getTopic(language);
            descriptionText = topic.getDescription(language);
        } else {
            topicText = topic.getTopic(language) + " (" + topic.getTopic(Language.DE) + ")";
            descriptionText = topic.getDescription(Language.DE) + "\n\n- - - - - - - - - - - -\n\n" + topic.getDescription(language);
        }
        String assignmentText = localizedMessagesService.assignmentText(language,
                topicText,
                descriptionText,
                topic.getKeywords(language));

        Message message = sendMessageWithButton(chatId, assignmentText, localizedMessagesService.buttonIWantAnother(language), "new_assignment");
        assignmentService.setTelegramMessageId(assignment, message.getMessageId());
    }

    private void sendMessage(Long chatId, String text) {
        List<String> messageChunks = splitMessage(text, TELEGRAM_MESSAGE_LIMIT);

        for (String chunk : messageChunks) {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(chunk)
                    .parseMode("Markdown")
                    .build();
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                LOGGER.error("Error sending message chunk: {}", e.getMessage(), e);
            }
        }
    }

    private Message sendMessageWithButton(Long chatId, String text, String buttonText, String callbackData) {
        List<String> messageChunks = splitMessage(text, TELEGRAM_MESSAGE_LIMIT);
        Message lastSentMessage = null;

        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(buttonText)
                        .callbackData(callbackData)
                        .build())))
                .build();

        for (int i = 0; i < messageChunks.size(); i++) {
            boolean isLastChunk = (i == messageChunks.size() - 1);

            SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder()
                    .chatId(chatId)
                    .text(messageChunks.get(i))
                    .parseMode("Markdown");

            if (isLastChunk) {
                messageBuilder.replyMarkup(keyboard);
            }

            try {
                lastSentMessage = telegramClient.execute(messageBuilder.build());
            } catch (TelegramApiException e) {
                LOGGER.error("Error sending message chunk with button: {}", e.getMessage(), e);
            }
        }

        return lastSentMessage;
    }

    private List<String> splitMessage(String text, int limit) {
        List<String> chunks = new ArrayList<>();

        while (text.length() > limit) {
            int splitIndex = text.lastIndexOf("\n", limit); // Try to split at newline
            if (splitIndex == -1) {
                splitIndex = text.lastIndexOf(" ", limit); // Otherwise split at the last space
            }
            if (splitIndex == -1) {
                splitIndex = limit; // If no space, force split at limit
            }

            chunks.add(text.substring(0, splitIndex).trim());
            text = text.substring(splitIndex).trim();
        }

        if (!text.isEmpty()) {
            chunks.add(text);
        }

        return chunks;
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

    private void showTyping(Long chatId) {
        SendChatAction sendChatAction = SendChatAction.builder()
                .action(ActionType.TYPING.toString())
                .chatId(chatId)
                .build();
        try {
            telegramClient.execute(sendChatAction);
            // LOGGER.info("Sending 'typing' action to chat {}", chatId);
        } catch (TelegramApiException e) {
            LOGGER.error("Error sending 'typing' action");
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
