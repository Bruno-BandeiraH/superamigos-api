package com.superamigos.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserCreationData(
    @NotBlank String name,
    @NotBlank String username,
    @NotBlank String password) {
}
