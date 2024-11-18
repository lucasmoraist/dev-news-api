package com.devnews.api.service.impl;

import com.devnews.api.domain.dto.comment.CommentRequest;
import com.devnews.api.domain.dto.comment.CommentResponse;
import com.devnews.api.domain.entity.Comment;
import com.devnews.api.domain.entity.Post;
import com.devnews.api.domain.entity.User;
import com.devnews.api.repository.CommentRepository;
import com.devnews.api.service.CommentService;
import com.devnews.api.service.PostService;
import com.devnews.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final PostService postService;
    private final UserService userService;
    private final CommentRepository repository;

    public CommentServiceImpl(PostService postService, UserService userService, CommentRepository repository) {
        this.postService = postService;
        this.userService = userService;
        this.repository = repository;
    }

    @Transactional
    @Override
    public CommentResponse addComment(Long postId, CommentRequest request) {
        Post post = this.postService.getPostEntityById(postId);
        log.info("Post encontrado: {}", post);

        String email = this.recoverLoggedEmail();
        User user = this.userService.getUserByEmail(email);
        log.info("Usuário encontrado: {}", user);

        Comment comment = Comment.builder()
                .content(request.content())
                .post(post)
                .user(user)
                .build();
        this.repository.save(comment);
        log.info("Comentário salvo");

        return new CommentResponse(comment.getId(), comment.getContent(), user.getName());
    }

    @Override
    public CommentResponse updateComment(Long commentId, CommentRequest request) {
        Comment comment = this.getCommentById(commentId);

        String email = this.recoverLoggedEmail();
        if (!comment.getUser().getEmail().equals(email)) {
            log.error("Permissão negada para editar comentário.");
            throw new IllegalArgumentException("Você não tem permissão para editar este comentário.");
        }

        comment.setContent(request.content());
        this.repository.save(comment);
        log.info("Comentário atualizado");

        return new CommentResponse(comment.getId(), comment.getContent(), comment.getUser().getName());
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = this.getCommentById(commentId);

        String email = this.recoverLoggedEmail();
        if (!comment.getUser().getEmail().equals(email)) {
            log.error("Permissão negada para deletar comentário.");
            throw new IllegalArgumentException("Você não tem permissão para deletar este comentário.");
        }

        this.repository.delete(comment);
        log.info("Comentário deletado");
    }

    private Comment getCommentById(Long id) {
        log.info("Buscando comentário por id: {}", id);
        Comment comment = this.repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Comentário não encontrado.");
                    return new IllegalArgumentException("Comentário não encontrado.");
                });
        log.info("Comentário encontrado: {}", comment);

        return comment;
    }

    private String recoverLoggedEmail() {
        // Recupera o email do usuário logado a partir do token passado na requisição
        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (loggedEmail.equals("anonymousUser")) {
            throw new IllegalArgumentException("Você precisa não está logado para realizar esta ação.");
        }
        return loggedEmail;
    }
}
