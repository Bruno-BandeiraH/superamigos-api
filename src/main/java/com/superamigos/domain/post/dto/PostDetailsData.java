package com.superamigos.domain.post.dto;

import com.superamigos.domain.comment.Comment;
import com.superamigos.domain.post.Post;
import com.superamigos.domain.user.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailsData(
    @NotNull
    Long id,
    String content,
    String authorName,
//    List<Comment> comments,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
    public PostDetailsData(Post post) {
        this(post.getId(),
            post.getContent(),
            post.getAuthor().getName(),
//            post.getComments(),
            post.getCreatedAt(),
            post.getUpdatedAt());
    }
}
