package com.devnews.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String token;

    public String generateToken(String email) {
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

}
