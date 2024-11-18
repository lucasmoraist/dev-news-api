package com.devnews.api.service.impl;

import com.devnews.api.domain.dto.user.LoginRequest;
import com.devnews.api.domain.dto.user.LoginResponse;
import com.devnews.api.domain.dto.user.UserRequest;
import com.devnews.api.domain.entity.User;
import com.devnews.api.repository.UserRepository;
import com.devnews.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void registerUser(UserRequest request) {
        log.info("Cadastrando usuário com email: {}", request.email());
        var existsEmail = this.repository.findByEmail(request.email());

        if (existsEmail.isPresent()) {
            log.error("Email já cadastrado: {}", request.email());
            throw new IllegalArgumentException("Email já cadastrado");
        }

        User user = new User(request);
        log.info("Salvando usuário: {}", user);

        this.repository.save(user);
        log.info("Usuário cadastrado com sucesso");
    }

    @Override
    public LoginResponse signIn(LoginRequest request) {
        log.info("Autenticando usuário com email: {}", request.email());

        User user = this.repository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado com email: {}", request.email());
                    return new IllegalArgumentException("Usuário não encontrado");
                });

        if (!request.password().equals(user.getPassword())) {
            log.error("Senha inválida para o usuário com email: {}", request.email());
            throw new IllegalArgumentException("Senha inválida");
        }

        log.info("Usuário autenticado com sucesso: {}", user);

        return new LoginResponse("token", user.getEmail());
    }
}
