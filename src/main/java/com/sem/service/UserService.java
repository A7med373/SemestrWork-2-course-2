package com.sem.service;

import com.sem.dto.UserRegDto;
import com.sem.models.user.Role;
import com.sem.models.user.User;
import com.sem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(UserRegDto userDto) {

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);


        return savedUser;
    }

    @Transactional
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new SecurityException("Current password is incorrect");
        }


        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

}