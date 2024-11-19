package com.devnews.api.infra.security;

import com.devnews.api.domain.entity.User;
import com.devnews.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserDetailsServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("loadUserByUsername deve retornar UserDetails quando usuário existe")
    void case01() {
        // Arrange
        String username = "test@example.com";
        User mockUser = new User(); // Substitua pelo construtor ou builder apropriado
        mockUser.setEmail(username);

        when(repository.findByEmail(username)).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails userDetails = service.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        verify(repository, times(1)).findByEmail(username);
    }

    @Test
    @DisplayName("loadUserByUsername deve lançar exceção quando usuário não existe")
    void case02() {
        // Arrange
        String username = "notfound@example.com";

        when(repository.findByEmail(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(username);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(repository, times(1)).findByEmail(username);
    }

}