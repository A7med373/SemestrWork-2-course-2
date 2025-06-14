package com.sem.service;

import com.sem.dto.UserProfileUpdateDto;
import com.sem.models.user.UserProfile;
import com.sem.repository.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final BookProfileService bookProfileService;
    private final CloudinaryService cloudinaryService;
    private final AuthorizationService authorizationService;

    @Transactional(readOnly = true)
    public UserProfile getUserProfileById(UUID id) {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found"));
    }

    @Transactional
    public UserProfile updateUserProfile(UUID userId, UserProfile updatedProfile) {
        // Проверка прав доступа
        if (!authorizationService.hasAccess(userId, "USER")) {
            throw new AccessDeniedException("You can only update your own profile");
        }

        UserProfile existingProfile = getUserProfileById(userId);
        existingProfile.setFirstName(updatedProfile.getFirstName());
        existingProfile.setLastName(updatedProfile.getLastName());
        existingProfile.setDescription(updatedProfile.getLastName());

        return userProfileRepository.save(existingProfile);
    }

    @Transactional
    public String updateUserAvatar(UUID userId, MultipartFile avatarFile) throws IOException {
        if (!authorizationService.hasAccess(userId, "USER")) {
            throw new AccessDeniedException("You can only update your own avatar");
        }

        UserProfile profile = getUserProfileById(userId);
        String newAvatarUrl = cloudinaryService.uploadAvatar(avatarFile, userId);
        profile.setAvatarUrl(newAvatarUrl);
        userProfileRepository.save(profile);
        return newAvatarUrl;
    }

    @Transactional
    public void addBookToRead(UUID userId, Long bookId) {
        UserProfile user = getUserProfileById(userId);
        user.getBooksRead().add(bookProfileService.getBookById(bookId));
        userProfileRepository.save(getUserProfileById(userId));
    }

    @Transactional
    public List<UserProfile> searchUsersByName(String name){
        return userProfileRepository.findByName(name);
    }

    @Transactional
    public UserProfile updateUserProfile(UUID userId, UserProfileUpdateDto updateDto) {
        if (!authorizationService.hasAccess(userId, "USER")) {
            throw new AccessDeniedException("You can only update your own profile");
        }

        UserProfile existingProfile = getUserProfileById(userId);
        existingProfile.setFirstName(updateDto.getFirstName());
        existingProfile.setLastName(updateDto.getLastName());

        // Важное исправление: установка description вместо lastName
        existingProfile.setDescription(updateDto.getDescription());

        return userProfileRepository.save(existingProfile);
    }
}