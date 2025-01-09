package com.serhiidrahan.daily_sochinenie_de.service;

import com.serhiidrahan.daily_sochinenie_de.entity.User;
import com.serhiidrahan.daily_sochinenie_de.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User getUser(Long telegramUserId, String telegramUsername, Long chatId) {
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
        return userRepository.save(newUser);
    }
}