package com.devnews.api.service;

import com.devnews.api.domain.dto.post.PostRequest;
import com.devnews.api.domain.dto.post.PostResponse;
import com.devnews.api.domain.dto.post.PostSearchResponse;
import com.devnews.api.domain.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    PostResponse savePost(PostRequest request);
    Page<PostResponse> getPosts(int page, int size);
    List<PostSearchResponse> searchPost(String title);
    PostResponse getPostById(Long id);
    PostResponse updatePost(Long id, PostRequest request);
    void deletePostById(Long id);
    Post getPostEntityById(Long id);
}
