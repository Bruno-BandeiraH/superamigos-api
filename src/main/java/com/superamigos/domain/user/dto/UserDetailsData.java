package com.superamigos.domain.user.dto;

import com.superamigos.domain.user.User;
import jakarta.validation.constraints.NotNull;

public record UserDetailsData(
    @NotNull
    Long id,
    String name,
    String username,
    String statusPhrase) {
    public UserDetailsData(User user) {
        this(user.getId(), user.getName(), user.getUsername(), user.getStatusPhrase());
    }

    public UserDetailsData(UserDetailsData data) {
        this(data.id(), data.name(), data.username(), data.statusPhrase());
    }
}
