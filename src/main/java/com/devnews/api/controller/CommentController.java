package com.devnews.api.controller;

import com.devnews.api.domain.dto.comment.CommentRequest;
import com.devnews.api.domain.dto.comment.CommentResponse;
import com.devnews.api.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/comment")
@Tag(name = "Comment", description = "Requisições relacionadas a comentários")
public class CommentController {

    @Autowired
    private CommentService service;

    @PostMapping("v2/{postId}")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long postId, @RequestBody CommentRequest request) {
        log.info("Recebendo requisição para adicionar comentário no post de id: {}", postId);
        var response = this.service.addComment(postId, request);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("v2/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentRequest request) {
        var response = this.service.updateComment(commentId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("v2/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        this.service.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}
