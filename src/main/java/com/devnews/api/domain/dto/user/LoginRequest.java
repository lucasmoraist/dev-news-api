package com.devnews.api.domain.dto.user;

public record LoginRequest(
        String email,
        String password
) {
}
