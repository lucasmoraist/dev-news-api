package com.devnews.api.service;

import com.devnews.api.domain.dto.user.LoginRequest;
import com.devnews.api.domain.dto.user.LoginResponse;
import com.devnews.api.domain.dto.user.UserRequest;

public interface UserService {
    void registerUser(UserRequest request);
    LoginResponse signIn(LoginRequest request);
}
