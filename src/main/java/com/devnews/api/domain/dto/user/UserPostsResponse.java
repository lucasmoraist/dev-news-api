package com.devnews.api.domain.dto.user;

import com.devnews.api.domain.entity.Post;

public record UserPostsResponse(
        Long id,
        String title,
        String imageBanner,
        String updatedAt
) {
    public UserPostsResponse(Post post) {
        this(post.getId(), post.getTitle(), post.getImageBanner(), post.getUpdatedAt().toString());
    }
}
