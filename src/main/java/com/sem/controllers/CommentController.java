package com.sem.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {
    @GetMapping("/review/{reviewId}/comments")
    public String commentsForReview(@PathVariable Long id){
        return "comments";
    }
}
