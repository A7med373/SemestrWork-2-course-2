package com.sem.controllers;

import com.sem.dto.mapper.BookMapper;
import com.sem.dto.mapper.ReviewMapper;
import com.sem.models.books.BookProfile;
import org.springframework.web.bind.annotation.RestController;

import com.sem.dto.BookResponseDTO;
import com.sem.dto.ReviewResponseDTO;
import com.sem.service.BookProfileService;
import com.sem.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MainPageController {

    private final BookProfileService bookService;
    private final ReviewService reviewService;

    @GetMapping("/")
    public String home(Model model) {
        // Получаем новинки книг
        List<BookResponseDTO> newBooks = bookService.findNewestBooks(10,0).stream().map(BookMapper::toDto).collect(Collectors.toList());
        model.addAttribute("newBooks", newBooks);

        // Получаем последние рецензии
        List<ReviewResponseDTO> recentReviews = reviewService.findNewestReviews(10, 0).stream().map(ReviewMapper::toDto).toList();
        model.addAttribute("recentReviews", recentReviews);

        return "mainPage";
    }
}
