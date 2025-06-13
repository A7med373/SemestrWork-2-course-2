package com.sem.dto.mapper;

import com.sem.dto.ReviewResponseDTO;
import com.sem.models.review.Review;

import java.util.stream.Collectors;

public class ReviewMapper {
    public static ReviewResponseDTO toDto(Review review){
        new ReviewResponseDTO();
        return ReviewResponseDTO.builder().
                id(review.getId()).
                user(review.getUser().getId()).
                text(review.getText()).
                createdAt(review.getCreatedAt())
                .build();
    }
}
