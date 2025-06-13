package com.sem.service;

import com.sem.models.review.Comment;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserProfileRepository userRepository;
    private final AuthorizationService authorizationService;

    @Transactional
    public Comment addComment(Long reviewId, UUID authorId, String commentText) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        UserProfile author = userRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setComment(commentText);
        comment.setReview(review);
        comment.setAuthor(author);

        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, UUID userId) {
        Comment comment = getCommentById(commentId);

        boolean isAuthor = comment.getAuthor().getId().equals(userId);
        boolean isReviewAuthor = comment.getReview().getUser().getId().equals(userId);
        boolean isAdmin = authorizationService.hasAccess(userId, "ADMIN");

        if (!isAuthor && !isReviewAuthor && !isAdmin) {
            throw new AccessDeniedException("You don't have permission to delete this comment");
        }

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsForReview(Long reviewId) {
        return commentRepository.findByReviewId(reviewId);
    }

    @Transactional(readOnly = true)
    public List<Comment> getUserComments(UUID userId) {
        return commentRepository.findByAuthorId(userId);
    }

    @Transactional()
    public List<Comment> searchComments(String query) {
        return commentRepository.findByComment(query);
    }
}
