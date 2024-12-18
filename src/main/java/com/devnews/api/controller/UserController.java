package com.devnews.api.controller;

import com.devnews.api.domain.dto.user.LoginRequest;
import com.devnews.api.domain.dto.user.LoginResponse;
import com.devnews.api.domain.dto.user.UserRequest;
import com.devnews.api.domain.dto.user.UserResponse;
import com.devnews.api.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Requisições relacionadas a usuários")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("v1/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid UserRequest request) {
        log.info("Recebendo requisição para salvar um usuário: {}", request);
        this.service.registerUser(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("v1/signin")
    public ResponseEntity<LoginResponse> signin(@RequestBody @Valid LoginRequest request) {
        log.info("Recebendo requisição para logar um usuário: {}", request);
        var response = this.service.signIn(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("v2")
    public ResponseEntity<UserResponse> getUser() {
        log.info("Recebendo requisição para buscar um usuário");
        var response = this.service.getUser();
        return ResponseEntity.ok().body(response);
    }

}
