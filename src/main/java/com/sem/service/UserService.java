package com.sem.service;

import com.sem.models.user.User;
import com.sem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordHistoryService passwordHistoryService;

    @Transactional
    public User registerUser(User user) {
        validatePassword(user.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        passwordHistoryService.savePasswordHistory(savedUser, user.getPassword(), Instant.now());

        return savedUser;
    }

    @Transactional
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new SecurityException("Current password is incorrect");
        }

        validatePassword(newPassword);

        if (passwordHistoryService.isPasswordUsed(userId, newPassword)) {
            throw new SecurityException("Password was used recently");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordHistoryService.savePasswordHistory(user, user.getPassword(), Instant.now());
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 12) {
            throw new IllegalArgumentException("Password must be at least 12 characters");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain uppercase letters");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain lowercase letters");
        }

        if (!password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must contain digits");
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            throw new IllegalArgumentException("Password must contain special characters");
        }
    }
}