package com.devnews.api.domain.dto;

import com.devnews.api.domain.entity.Post;

public record PostSearchResponse(
        Long id,
        String title
) {
    public PostSearchResponse(Post post) {
        this(post.getId(), post.getTitle());
    }
}
