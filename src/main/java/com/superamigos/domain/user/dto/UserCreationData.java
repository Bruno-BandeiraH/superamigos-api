package com.superamigos.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreationData(
    @NotBlank String name,
    @NotBlank String username,
    @NotBlank @Size(min = 6, max = 18) String password) {
}
