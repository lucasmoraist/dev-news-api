package com.devnews.api.controller;

import com.devnews.api.domain.dto.post.PostListResponse;
import com.devnews.api.domain.dto.post.PostRequest;
import com.devnews.api.domain.dto.post.PostResponse;
import com.devnews.api.domain.dto.post.PostSearchResponse;
import com.devnews.api.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/post")
@Tag(name = "Post", description = "Requisições para manipulação de posts")
public class PostController {

    @Autowired
    private PostService service;

    @PostMapping("v2")
    public ResponseEntity<PostResponse> save(
            @RequestBody @Valid PostRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        log.info("Recebendo requisição para salvar um post: {}", request);

        var response = this.service.savePost(request);
        URI uri = uriBuilder
                .path("/post/v1")
                .build()
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PatchMapping("v2/{postId}/image")
    public ResponseEntity<PostResponse> uploadImage(
            @PathVariable Long postId,
            @RequestParam("file") MultipartFile file
    ) {
        log.info("Recebendo requisição para salvar imagem do post com id: {}", postId);
        var response = this.service.saveImage(postId, file);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("v1")
    public ResponseEntity<Page<PostListResponse>> pagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Recebendo requisição para buscar posts da página {} com tamanho {}", page, size);
        var response = this.service.getPosts(page, size);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("v1/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        log.info("Recebendo requisição para buscar post com id: {}", id);
        var response = this.service.getPostById(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("v1/find")
    public ResponseEntity<List<PostSearchResponse>> search(@RequestParam String title) {
        log.info("Recebendo requisição para buscar posts com título: {}", title);
        var response = this.service.searchPost(title);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("v2/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable Long id, @RequestBody PostRequest request) {
        log.info("Recebendo requisição para atualizar post com id: {}", id);
        var response = this.service.updatePost(id, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("v2/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Recebendo requisição para deletar post com id: {}", id);
        this.service.deletePostById(id);
        return ResponseEntity.noContent().build();
    }

}
