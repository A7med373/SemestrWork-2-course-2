package com.sem.controllers;

import com.sem.service.BookProfileService;
import com.sem.service.CommentService;
import com.sem.service.ReviewService;
import com.sem.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class SearchController {

    private final BookProfileService bookProfileService;
    private final UserProfileService userProfileService;
    private final ReviewService reviewService;
    private final CommentService commentService;

    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            Model model) {

        if (query != null && !query.isEmpty() && category != null) {
            switch (category) {
                case "book":
                    model.addAttribute("books", bookProfileService.searchBooksByName(query));
                    model.addAttribute("booksByDesc", bookProfileService.searchBooksByDescription(query));
                    break;
                case "author":
                    model.addAttribute("authors", userProfileService.searchUsersByName(query));
                    break;
                case "review":
                    model.addAttribute("reviews", reviewService.searchReviews(query));
                    break;
                case "comment":
                    model.addAttribute("comments", commentService.searchComments(query));
                    break;
            }
        }

        model.addAttribute("query", query);
        model.addAttribute("category", category);
        return "search";
    }
}
