package com.sem.repository;

import com.sem.models.review.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByReviewId(Long reviewId);

    List<Comment> findByAuthorId(UUID authorId);

    List<Comment> findByComment(String text);

    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.id = :id")
    Optional<Comment> findByIdWithAuthor(@Param("id") Long id);

    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.review.id = :reviewId")
    List<Comment> findByReviewIdWithAuthor(@Param("reviewId") Long reviewId);

    void deleteByAuthorId(UUID authorId);
}
