package com.devnews.api.domain.dto.post;

import com.devnews.api.domain.dto.comment.CommentResponse;
import com.devnews.api.domain.entity.Post;

import java.util.List;

public record PostResponse(
        String title,
        String content,
        String imageBanner,
        String author,
        String updatedAt,
        List<CommentResponse> comments
) {
    public PostResponse(Post post) {
        this(
                post.getTitle(),
                post.getContent(),
                post.getImageBanner(),
                post.getAuthor().getName(),
                post.getUpdatedAt().toString(),
                post.getComments()
                        .stream()
                        .map(CommentResponse::new)
                        .toList()
        );
    }
}
