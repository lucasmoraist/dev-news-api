package com.devnews.api.service.impl;

import com.devnews.api.domain.dto.post.PostListResponse;
import com.devnews.api.domain.dto.post.PostRequest;
import com.devnews.api.domain.dto.post.PostResponse;
import com.devnews.api.domain.dto.post.PostSearchResponse;
import com.devnews.api.domain.entity.Post;
import com.devnews.api.domain.entity.User;
import com.devnews.api.domain.exception.IllegalPermissionException;
import com.devnews.api.domain.exception.ResourceNotFound;
import com.devnews.api.repository.PostRepository;
import com.devnews.api.service.PostService;
import com.devnews.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final UserService userService;

    public PostServiceImpl(PostRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Transactional
    @Override
    public PostResponse savePost(PostRequest request) {
        String email = this.recoverLoggedEmail();
        User user = this.userService.getUserByEmail(email);

        Post post = new Post(request);
        post.setAuthor(user);
        this.repository.save(post);
        log.info("Post salvo com sucesso: {}", post);

        return new PostResponse(post);
    }

    @Override
    public Page<PostListResponse> getPosts(int page, int size) {
        log.info("Buscando posts da página {} com tamanho {}", page, size);
        Pageable pageable = Pageable
                .ofSize(size)
                .withPage(page);
        return this.repository.findAll(pageable)
                .map(PostListResponse::new);
    }

    @Override
    public List<PostSearchResponse> searchPost(String title) {
        log.info("Buscando posts com título: {}", title);
        return this.repository.findByTitle(title)
                .stream()
                .map(PostSearchResponse::new)
                .limit(5)
                .toList();
    }

    @Override
    public PostResponse getPostById(Long id) {
        Post post = this.getPostEntityById(id);
        return new PostResponse(post);
    }

    @Override
    public PostResponse updatePost(Long id, PostRequest request) {
        Post post = this.getPostEntityById(id);

        String email = this.recoverLoggedEmail();
        if (!post.getAuthor().getEmail().equals(email)) {
            log.error("Permissão negada para editar post.");
            throw new IllegalPermissionException("Você não tem permissão para editar este post.");
        }

        post.update(request);
        log.info("Atualizando post: {}", post);

        this.repository.save(post);
        log.info("Post atualizado com sucesso");

        return new PostResponse(post);
    }

    @Override
    public void deletePostById(Long id) {
        Post post = this.getPostEntityById(id);

        String email = this.recoverLoggedEmail();
        if (!post.getAuthor().getEmail().equals(email)) {
            log.error("Permissão negada para deletar post.");
            throw new IllegalPermissionException("Você não tem permissão para deletar este post.");
        }

        this.repository.delete(post);
        log.info("Post deletado com sucesso");
    }

    @Override
    public Post getPostEntityById(Long id) {
        log.info("Buscando post com id {}", id);
        Post post = this.repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Post com id {} não encontrado", id);
                    return new ResourceNotFound("Post não encontrado");
                });
        log.info("Post encontrado: {}", post);
        return post;
    }

    private String recoverLoggedEmail() {
        // Recupera o email do usuário logado a partir do token passado na requisição
        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (loggedEmail.equals("anonymousUser")) {
            throw new IllegalPermissionException("Você não está logado para realizar esta ação.");
        }
        return loggedEmail;
    }
}
