package com.sem.repository;


import com.sem.models.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserId(UUID userId);

    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.comments WHERE r.id = :id")
    Optional<Review> findByIdWithComments(@Param("id") Long id);

    @Query("SELECT r FROM Review r where r.review like CONCAT('%', :text, '%')")
    List<Review> findByReviewContainingIgnoreCase(@Param("text") String text);

    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.comments WHERE r.user.id = :userId")
    Optional<Review> findByUserIdWithComments(@Param("userId") UUID userId);

    @Query("SELECT COUNT(c) FROM Review r JOIN r.comments c WHERE r.id = :reviewId")
    int countCommentsByReviewId(@Param("reviewId") Long reviewId);

    @Query(value = "SELECT * FROM Review ORDER BY created_at DESC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Review> findNewestReviews(@Param("limit") int limit, @Param("offset") int offset);

    boolean existsByUserId(UUID userId);

    void deleteByUserId(UUID userId);
}
