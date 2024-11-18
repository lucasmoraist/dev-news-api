package com.devnews.api.infra.security;

import com.devnews.api.domain.entity.User;
import com.devnews.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Buscando usuário pelo email: {}", username);
        User user = this.repository.findByEmail(username)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado pelo email: {}", username);
                    return new UsernameNotFoundException("Usuário não encontrado");
                });
        return new UserDetailsImpl(user);
    }
}
