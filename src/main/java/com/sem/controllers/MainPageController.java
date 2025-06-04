package com.sem.controllers;

import com.sem.configs.CloudinaryConfig;
import com.sem.dto.mapper.BookMapper;
import com.sem.dto.mapper.ReviewMapper;
import com.sem.models.books.BookProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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

@Controller
@RequiredArgsConstructor
public class MainPageController {
    private static final Logger logger = LoggerFactory.getLogger(MainPageController.class);
    private final BookProfileService bookService;
    private final ReviewService reviewService;

    @GetMapping("/")
    public String home(Model model) {
        // Получаем новинки книг
        List<BookResponseDTO> newBooks = bookService.findNewestBooks(10,0).stream().map(BookMapper::toDto).collect(Collectors.toList());
        model.addAttribute("newBooks", newBooks);
        logger.debug("Books taken");
        // Получаем последние рецензии
        List<ReviewResponseDTO> recentReviews = reviewService.findNewestReviews(10, 0).stream().map(ReviewMapper::toDto).toList();
        model.addAttribute("recentReviews", recentReviews);
        logger.debug("reviews taken");
        return "mainPage";
    }
}
