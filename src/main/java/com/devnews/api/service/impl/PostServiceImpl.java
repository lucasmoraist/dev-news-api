package com.devnews.api.service.impl;

import com.devnews.api.domain.dto.PostRequest;
import com.devnews.api.domain.dto.PostResponse;
import com.devnews.api.domain.dto.PostSearchResponse;
import com.devnews.api.domain.entity.Post;
import com.devnews.api.repository.PostRepository;
import com.devnews.api.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repository;

    public PostServiceImpl(PostRepository repository) {
        this.repository = repository;
    }

    @Override
    public PostResponse savePost(PostRequest request) {
        Post post = this.repository.save(new Post(request));
        log.info("Post salvo com sucesso: {}", post);
        return new PostResponse(post);
    }

    @Override
    public Page<PostResponse> getPosts(int page, int size) {
        log.info("Buscando posts da página {} com tamanho {}", page, size);
        Pageable pageable = Pageable
                .ofSize(size)
                .withPage(page);
        return this.repository.findAll(pageable)
                .map(PostResponse::new);
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
        Post post = this.findPost(id);
        return new PostResponse(post);
    }

    @Override
    public PostResponse updatePost(Long id, PostRequest request) {
        Post post = this.findPost(id);
        post.update(request);
        log.info("Atualizando post: {}", post);
        this.repository.save(post);
        log.info("Post atualizado com sucesso");
        return new PostResponse(post);
    }

    @Override
    public void deletePostById(Long id) {
        Post post = this.findPost(id);
        log.info("Deletando post: {}", post);
        this.repository.delete(post);
        log.info("Post deletado com sucesso");
    }

    private Post findPost(Long id) {
        log.info("Buscando post com id {}", id);
        Post post = this.repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Post com id {} não encontrado", id);
                    return new RuntimeException("Post não encontrado");
                });
        log.info("Post encontrado: {}", post);
        return post;
    }
}
