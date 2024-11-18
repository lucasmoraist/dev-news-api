package com.devnews.api.service;

import com.devnews.api.domain.dto.PostRequest;
import com.devnews.api.domain.dto.PostResponse;
import com.devnews.api.domain.dto.PostSearchResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    PostResponse savePost(PostRequest request);
    Page<PostResponse> getPosts(int page, int size);
    List<PostSearchResponse> searchPost(String title);
    PostResponse getPostById(Long id);
    PostResponse updatePost(Long id, PostRequest request);
    void deletePostById(Long id);
}
