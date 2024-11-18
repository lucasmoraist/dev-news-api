package com.devnews.api.domain.dto.user;

public record LoginResponse(
        String token,
        String email
) {
}
