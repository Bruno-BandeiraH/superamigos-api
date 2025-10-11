package com.superamigos.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordData(
    @NotBlank String currentPassword,
    @NotBlank @Size(min = 6, max = 18) String newPassword
) {

}
