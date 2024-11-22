package com.devnews.api.domain.dto.post;

import com.devnews.api.domain.entity.Post;

public record PostListResponse(
        Long id,
        String title,
        String content,
        String imageBanner,
        String author,
        String updatedAt
) {
    public PostListResponse(Post post) {
        this(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImageBanner(),
                post.getAuthor().getName(),
                post.getUpdatedAt().toString()
        );
    }
}
