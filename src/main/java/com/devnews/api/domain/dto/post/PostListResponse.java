package com.devnews.api.domain.dto.post;

import com.devnews.api.domain.entity.Post;

public record PostListResponse(
        Long id,
        String title,
        String imageBanner,
        String updatedAt
) {
    public PostListResponse(Post post) {
        this(
                post.getId(),
                post.getTitle(),
                post.getImageBanner(),
                post.getUpdatedAt().toString()
        );
    }
}
