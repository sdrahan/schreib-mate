package com.serhiidrahan.schreib_mate_bot.service;

import com.serhiidrahan.schreib_mate_bot.entity.User;
import com.serhiidrahan.schreib_mate_bot.enums.Language;
import com.serhiidrahan.schreib_mate_bot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean userExists(Long telegramUserId) {
        Optional<User> existingUser = userRepository.findByTelegramId(telegramUserId);
        return existingUser.isPresent();
    }

    @Transactional
    public User getOrCreateUser(Long telegramUserId, String telegramUsername, Long chatId) {
        Optional<User> existingUser = userRepository.findByTelegramId(telegramUserId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // Update chat ID if changed
            if (!chatId.equals(user.getChatId())) {
                user.setChatId(chatId);
            }

            // Update username if changed
            if (telegramUsername != null && !telegramUsername.equals(user.getTelegramUsername())) {
                user.setTelegramUsername(telegramUsername);
                userRepository.save(user);
            }
            return user;
        }

        // Create a new user if not found
        User newUser = new User();
        newUser.setTelegramId(telegramUserId);
        newUser.setTelegramUsername(telegramUsername);
        newUser.setChatId(chatId);
        newUser.setLanguage(Language.DE);
        return userRepository.save(newUser);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}