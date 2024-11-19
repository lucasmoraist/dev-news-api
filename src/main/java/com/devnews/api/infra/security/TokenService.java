package com.devnews.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.devnews.api.domain.exception.IllegalPermissionException;
import com.devnews.api.domain.exception.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String token;

    public String generateToken(String email) {
        if (this.token == null || this.token.isEmpty()) {
            log.error("Chave secreta não configurada");
            throw new TokenException("Chave secreta não configurada");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.token);
            String token = JWT.create()
                    .withIssuer("devnews")
                    .withSubject(email)
                    .sign(algorithm);
            log.info("Token gerado com sucesso para o email: {}", email);
            return token;
        } catch (JWTCreationException e) {
            log.error("Erro ao gerar token para o email: {}", email, e);
            throw new RuntimeException("Erro ao gerar token: ", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.token);
            String subject = JWT.require(algorithm)
                    .withIssuer("devnews")
                    .build()
                    .verify(token)
                    .getSubject();
            log.info("Token validado com sucesso para o email: {}", subject);

            return subject;
        } catch (Exception e) {
            log.error("Erro ao validar token: {}", token, e);
            throw new RuntimeException("Token inválido: ", e);
        }
    }

    public String recoverLoggedEmail() {
        // Recupera o email do usuário logado a partir do token passado na requisição
        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (loggedEmail.equals("anonymousUser")) {
            throw new IllegalPermissionException("Você não está logado para realizar esta ação.");
        }
        return loggedEmail;
    }

}
