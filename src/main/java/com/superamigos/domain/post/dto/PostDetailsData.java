package com.superamigos.domain.post.dto;

import com.superamigos.domain.comment.dto.CommentDetailsData;
import com.superamigos.domain.post.Post;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PostDetailsData(
    @NotNull
    Long id,
    String content,
    String authorName,
    LocalDateTime createdAt,
    List<CommentDetailsData> comments,
    LocalDateTime updatedAt) {
    public PostDetailsData(Post post) {
        this(post.getId(),
            post.getContent(),
            post.getAuthor().getName(),
            post.getCreatedAt(),
            post.getComments().stream()
                    .map(CommentDetailsData::new)
                        .collect(Collectors.toList()),
            post.getUpdatedAt());
    }
}
