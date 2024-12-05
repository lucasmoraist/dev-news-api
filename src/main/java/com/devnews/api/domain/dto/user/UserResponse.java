package com.devnews.api.domain.dto.user;

import com.devnews.api.domain.entity.User;

import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String password,
        List<UserPostsResponse> posts
) {
    public UserResponse(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getPosts().stream().map(UserPostsResponse::new).toList());
    }
}
