package com.devnews.api.domain.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank
        @Size(min = 3, max = 180)
        String name,
        @Email
        @NotBlank
        @Size(max = 255)
        String email,
        @NotBlank
        @Size(min = 6, max = 255)
        String password
) {
}
