package com.sem.service;

import com.sem.models.review.Review;
import com.sem.models.user.UserProfile;
import com.sem.repository.CommentRepository;
import com.sem.repository.ReviewRepository;
import com.sem.repository.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserProfileRepository userProfileRepository;
    private final CommentRepository commentRepository;
    private final AuthorizationService authorizationService;

    @Transactional
    public Review createReview(UUID userId, String reviewText) {
        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Review review = new Review();
        review.setReview(reviewText);
        review.setUser(user);

        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
    }

    @Transactional
    public Review updateReview(Long reviewId, UUID userId, String newText) {
        Review review = getReviewById(reviewId);

        if (!review.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only update your own reviews");
        }

        review.setReview(newText);
        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long reviewId, UUID userId) {
        Review review = getReviewById(reviewId);

        if (!review.getUser().getId().equals(userId) &&
                !authorizationService.hasAccess(userId, "ADMIN")) {
            throw new AccessDeniedException("You don't have permission to delete this review");
        }

        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public List<Review> searchReviews(String text) {
        return reviewRepository.findByReviewContainingIgnoreCase(text);
    }

    @Transactional(readOnly = true)
    public Review getReviewWithComments(Long id) {
        return reviewRepository.findByIdWithComments(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
    }

    @Transactional(readOnly = true)
    public Optional<Review> getUserReviews(UUID userId) {
        return reviewRepository.findByUserId(userId);
    }
}
