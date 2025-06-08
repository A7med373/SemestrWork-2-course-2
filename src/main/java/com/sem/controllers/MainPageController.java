package com.sem.controllers;

import com.sem.dto.BookResponseDTO;
import com.sem.dto.ReviewResponseDTO;
import com.sem.dto.mapper.BookMapper;
import com.sem.dto.mapper.PaginationMapper;
import com.sem.dto.mapper.ReviewMapper;
import com.sem.models.books.BookProfile;
import com.sem.models.review.Review;
import com.sem.models.user.User;
import com.sem.service.BookProfileService;
import com.sem.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.print.Book;

@Controller
@RequiredArgsConstructor
public class MainPageController {

    private final BookProfileService bookService;
    private final ReviewService reviewService;

    // Традиционный запрос для полной загрузки страницы (без AJAX)
    @GetMapping("/")
    public String mainPage(
            @AuthenticationPrincipal User currentUser,
            Model model) {

        // Добавляем пользователя в модель для шапки
        model.addAttribute("user", currentUser);

        // Загружаем данные для SEO/первоначального рендеринга
        // (можно ограничить количество элементов для первоначальной загрузки)
        Page<BookProfile> newBooks = PaginationMapper.listToPage(bookService.findNewestBooks(5, 0), 0 , 5);

        Page<Review> recentReviews = PaginationMapper.listToPage(reviewService.findNewestReviews(5,0),0, 5);

        model.addAttribute("newBooks", newBooks.getContent());
        model.addAttribute("recentReviews", recentReviews.getContent());

        return "mainPage";
    }

    // AJAX-эндпоинт для загрузки новинок (вызывается из JavaScript)
    @GetMapping("/api/books/new")
    @ResponseBody
    public ResponseEntity<Page<BookResponseDTO>> getNewBooks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<BookResponseDTO> booksPage = PaginationMapper.listToPage(bookService.findNewestBooks(size, page*size).stream().map(BookMapper::toDto).toList(), page, size);

        return ResponseEntity.ok(booksPage);
    }

    // AJAX-эндпоинт для загрузки рецензий
    @GetMapping("/api/reviews/recent")
    @ResponseBody
    public ResponseEntity<Page<ReviewResponseDTO>> getRecentReviews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<ReviewResponseDTO> reviewsPage = PaginationMapper.listToPage(reviewService.findNewestReviews(size, page*size).stream().map(ReviewMapper::toDto).toList(), page, size);

        return ResponseEntity.ok(reviewsPage);
    }
}
