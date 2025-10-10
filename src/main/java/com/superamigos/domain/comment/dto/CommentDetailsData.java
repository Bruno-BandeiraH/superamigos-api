package com.superamigos.domain.comment.dto;

import com.superamigos.domain.comment.Comment;

public record CommentDetailsData(Long id, String content, String authorUsername, String postContent) {
    public CommentDetailsData(Comment comment) {
        this(comment.getId(), comment.getContent(), comment.getAuthor().getUsername(), comment.getPost().getContent());
    }
}
