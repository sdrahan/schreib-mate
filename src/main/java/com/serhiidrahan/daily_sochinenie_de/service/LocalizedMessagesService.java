package com.serhiidrahan.daily_sochinenie_de.service;

import com.serhiidrahan.daily_sochinenie_de.enums.Language;
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

    private Locale getLocale(Language language) {
        return switch (language) {
            case RU -> new Locale("ru", "RU");
            case DE -> new Locale("de", "DE");
            default -> Locale.ENGLISH;
        };
    }
}
