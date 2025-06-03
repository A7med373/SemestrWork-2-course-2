package com.sem.dto.mapper;

import com.sem.dto.CommentResponseDTO;
import com.sem.models.review.Comment;

public class CommentMapper {
    public static CommentResponseDTO toDto(Comment comment){
        new CommentResponseDTO();
        return CommentResponseDTO.builder().
                id(comment.getId()).
                text(comment.getComment()).
                user(comment.getAuthor()).
                createdAt(comment.getCreatedAt()).
                build();
    }
}
