package com.devnews.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userService;
    private final TokenService tokenService;

    public SecurityFilter(UserDetailsServiceImpl userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);

        if (token != null) {
            log.info("Token recuperado: {}", token);
            var login = this.tokenService.validateToken(token);

            if (login != null) {
                var user = this.userService.loadUserByUsername(login);

                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Usuário autenticado: {}", login);

                } else {
                    log.warn("Usuário não encontrado para o login: {}", login);
                }

            } else {
                log.warn("Token inválido: {}", token);
            }
        } else {
            log.info("Nenhum token encontrado na requisição.");
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest req) {
        var authHeader = req.getHeader("Authorization");
        if (authHeader == null) {
            log.info("Cabeçalho de autorização não encontrado.");
            return null;
        }
        var token = authHeader.replace("Bearer ", "");
        log.info("Token recuperado do cabeçalho de autorização.");
        return token;
    }

}
