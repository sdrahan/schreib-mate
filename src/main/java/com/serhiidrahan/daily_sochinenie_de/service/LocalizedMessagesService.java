package com.serhiidrahan.daily_sochinenie_de.service;

import com.serhiidrahan.daily_sochinenie_de.enums.Language;
import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocalizedMessagesService {

    private final MessageSource messageSource;

    public LocalizedMessagesService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String submissionTopicIsWrong(Language language, String topic) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("submission.topic_wrong", new Object[]{topic}, locale);
    }

    public String sumbissionTooShort(Language language) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("submission.too_short",null, locale);
    }

    public String sumbissionTooLong(Language language, int maxChars) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("submission.too_long",new Object[]{maxChars}, locale);
    }

    public String assignmentText(Language language, String topic, String description, String keywords) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("submission.new_topic",new Object[]{topic, description, keywords}, locale);
    }

    public String languageConfirmation(Language language) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("settings.language_confirmation", null, locale);
    }

    public String languageSelect() {
        Locale locale = getLocale(Language.EN);
        return messageSource.getMessage("settings.language_select", null, locale);
    }

    private Locale getLocale(Language language) {
        return switch (language) {
            case RU -> new Locale("ru", "RU");
            case DE -> new Locale("de", "DE");
            default -> Locale.ENGLISH;
        };
    }

    public String errorProcessingImage(Language language) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("error.processing_image", null, locale);
    }

    public String errorGettingFeedback(Language language) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("error.getting_feedback", null, locale);
    }

    public String emptyImage(Language language) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("error.empty_image", null, locale);
    }

    public String errorNoTopicsLeft(Language language) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("error.no_topics_left", null, locale);
    }

    public String buttonIWantAnother(Language language) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("button.i_want_another", null, locale);
    }

    public String buttonIAmDone(Language language) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("button.i_am_done", null, locale);
    }

    public String firstAssignment(Language language) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("topic.first", null, locale);
    }

    public String wantAnotherTopic(Language language) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("topic.skipped", null, locale);
    }

    public String doneWithTopic(Language language) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("topic.done", null, locale);
    }

    public String privacyPolicy(Language language) {
        Locale locale = getLocale(language);
        return messageSource.getMessage("info.privacy_policy", null, locale);
    }
}
