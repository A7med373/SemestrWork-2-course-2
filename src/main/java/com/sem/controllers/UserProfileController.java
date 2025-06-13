package com.sem.controllers;

import com.sem.dto.UserProfileUpdateDto;
import com.sem.jwt.JwtTokenProvider;
import com.sem.models.user.UserProfile;
import com.sem.service.AuthorizationService;
import com.sem.service.BookProfileService;
import com.sem.service.UserProfileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final BookProfileService bookProfileService;
    private final AuthorizationService authorizationService;
    private final JwtTokenProvider jwtTokenProvider;
    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @GetMapping("/{id}")
    public String getUserProfile(
                                 @PathVariable UUID id,
                                 Model model) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();
        logger.info(((Boolean) id.toString().equals(currentUserId)).toString());
        model.addAttribute("isOwner", id.toString().equals(currentUserId));


        UserProfile userProfile = userProfileService.getUserProfileById(id);
        model.addAttribute("userProfile", userProfile);

        // Получаем книги, написанные пользователем
        model.addAttribute("authoredBooks", userProfileService.getUserProfileById(id).getBooksWritten());

        // Получаем книги, прочитанные пользователем
        model.addAttribute("readBooks", userProfileService.getUserProfileById(id).getBooksRead());

        return "userProfile";
    }

    @PostMapping("/{id}/update")
    @ResponseBody
    public Map<String, Object> updateProfile(
            @PathVariable UUID id,
            @RequestBody UserProfileUpdateDto updateDto) {

        Map<String, Object> response = new HashMap<>();
        try {
            logger.info("Updating profile for user: {}", id);
            UserProfile updatedProfile = userProfileService.updateUserProfile(id, updateDto);

            response.put("status", "success");
            response.put("firstName", updatedProfile.getFirstName());
            response.put("lastName", updatedProfile.getLastName());
            response.put("description", updatedProfile.getDescription());
        } catch (Exception e) {
            logger.error("Profile update error", e);
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PostMapping("/{id}/update-avatar")
    @ResponseBody
    public ResponseEntity<?> updateAvatar(
            @PathVariable UUID id,
            @RequestParam("avatar") MultipartFile file) {
        try {
            String newAvatarUrl = userProfileService.updateUserAvatar(id, file);
            return ResponseEntity.ok().body(Collections.singletonMap("avatarUrl", newAvatarUrl));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка обновления аватара: " + e.getMessage());
        }
    }
}