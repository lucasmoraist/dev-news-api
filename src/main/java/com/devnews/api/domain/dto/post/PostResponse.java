package com.devnews.api.domain.dto.post;

import com.devnews.api.domain.entity.Post;

public record PostResponse(
        Long id,
        String title,
        String content,
        String imageBanner,
        String createdAt,
        String updatedAt
) {
    public PostResponse(Post post) {
        this(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImageBanner(),
                post.getCreatedAt().toString(),
                post.getUpdatedAt().toString()
        );
    }
}
