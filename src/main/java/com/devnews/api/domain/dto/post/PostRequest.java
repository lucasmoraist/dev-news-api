package com.devnews.api.domain.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotBlank
        @Size(max = 120)
        String title,
        String content,
        String imageBanner
) {
}
