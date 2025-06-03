package com.sem.service;

import com.sem.models.user.PasswordHistory;
import com.sem.models.user.User;
import com.sem.models.user.UserProfile;
import com.sem.repository.PasswordHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordHistoryService {
    private final PasswordHistoryRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void savePasswordHistory(User user, String passwordHash, Instant now) {
        PasswordHistory history = new PasswordHistory();
        history.setUserId(user);
        history.setPasswordHash(passwordHash);
        history.setCreatedAt(now);
        repository.save(history);
    }

    public boolean isPasswordUsed(UUID userId, String password) {
        List<PasswordHistory> histories = repository.findLastByUserIdOrderByCreatedAtDesc(userId);
        return histories.stream()
                .anyMatch(history -> passwordEncoder.matches(password, history.getPasswordHash()));
    }
}
