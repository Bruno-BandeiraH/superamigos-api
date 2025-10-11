package com.superamigos.domain.post.dto;

import jakarta.validation.constraints.NotBlank;

public record PostCreationData(@NotBlank String content) {
}
