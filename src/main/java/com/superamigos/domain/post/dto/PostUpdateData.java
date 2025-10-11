package com.superamigos.domain.post.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PostUpdateData(@NotNull @Positive Long id, String content) {
}
