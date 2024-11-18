package com.devnews.api.domain.dto.comment;

import com.devnews.api.domain.entity.Comment;

public record CommentResponse(
        Long id,
        String content,
        String nameUser
) {
    public CommentResponse(Comment comment) {
        this(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getName()
        );
    }
}
