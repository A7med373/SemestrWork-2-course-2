package com.sem.controllers;

import com.sem.models.user.UserProfile;
import com.sem.service.AuthorizationService;
import com.sem.service.BookProfileService;
import com.sem.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final BookProfileService bookProfileService;
    private final AuthorizationService authorizationService;

    @GetMapping("/{id}")
    public String getUserProfile(
            @PathVariable UUID id,
            Authentication authentication,
            Model model) {

        // Получаем профиль пользователя
        UserProfile userProfile = userProfileService.getUserProfileById(id);
        model.addAttribute("userProfile", userProfile);

        // Проверяем, является ли текущий пользователь владельцем профиля
        boolean isOwner = authentication.getName().equals(userProfile.getEmail());
        model.addAttribute("isOwner", isOwner);

        // Получаем книги, написанные пользователем
        model.addAttribute("authoredBooks", userProfileService.getUserProfileById(id).getBooksWritten());

        // Получаем книги, прочитанные пользователем
        model.addAttribute("readBooks", userProfileService.getUserProfileById(id).getBooksRead());

        return "userProfile";
    }
}