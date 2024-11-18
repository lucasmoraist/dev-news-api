package com.devnews.api.service;

import com.devnews.api.domain.dto.comment.CommentRequest;
import com.devnews.api.domain.dto.comment.CommentResponse;

public interface CommentService {
    CommentResponse addComment(Long postId, CommentRequest request);
    CommentResponse updateComment(Long commentId, CommentRequest request);
    void deleteComment(Long commentId);
}
