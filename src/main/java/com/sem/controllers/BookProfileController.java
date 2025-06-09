package com.sem.controllers;

import com.sem.models.books.BookProfile;
import com.sem.service.BookProfileService;
import com.sem.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookProfileController {

    private final BookProfileService bookProfileService;
    private final AuthorizationService authorizationService;

    @GetMapping("/{id}")
    public String getBookProfile(@PathVariable Long id, Model model) {
        BookProfile book = bookProfileService.getBookById(id);
        model.addAttribute("book", book);
        return "book-profile";
    }

    @GetMapping("/{id}/edit")
    public String editBookForm(@PathVariable Long id,
                               Authentication authentication,
                               Model model) {
        BookProfile book = bookProfileService.getBookById(id);

        // Проверка прав доступа
        UUID currentUserId = authorizationService.getCurrentUserId(authentication);
        if (!book.getAuthor().getId().equals(currentUserId)) {
            return "redirect:/books/" + id + "?error=access_denied";
        }

        model.addAttribute("book", book);
        return "book-edit";
    }

    @PostMapping("/{id}/edit")
    public String updateBookProfile(@PathVariable Long id,
                                    @ModelAttribute BookProfile updatedBook,
                                    Authentication authentication) {
        UUID currentUserId = authorizationService.getCurrentUserId(authentication);
        bookProfileService.updateBookProfile(id, updatedBook, currentUserId);
        return "redirect:/books/" + id + "?success=updated";
    }
}