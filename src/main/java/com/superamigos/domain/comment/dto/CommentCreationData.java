package com.superamigos.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentCreationData(@NotBlank String content) {
}
