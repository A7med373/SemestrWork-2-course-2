package com.sem.dto;

import com.sem.models.review.Review;
import com.sem.models.user.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {
    private Long id;
    private UUID user;
    private String text;
    private Date createdAt;
    private List<CommentResponseDTO> comments;

}