package com.serhiidrahan.daily_sochinenie_de.repository;

import com.serhiidrahan.daily_sochinenie_de.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByTelegramId(Long telegramId);
}