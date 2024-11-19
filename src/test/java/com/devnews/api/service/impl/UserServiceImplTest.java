package com.devnews.api.service.impl;

import com.devnews.api.domain.dto.user.LoginRequest;
import com.devnews.api.domain.dto.user.LoginResponse;
import com.devnews.api.domain.dto.user.UserRequest;
import com.devnews.api.domain.entity.User;
import com.devnews.api.domain.exception.AlreadyExistException;
import com.devnews.api.domain.exception.CredentialsAccessException;
import com.devnews.api.infra.security.TokenService;
import com.devnews.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve cadastrar um usuário com sucesso")
    void case01() {
        // Arrange
        UserRequest request = new UserRequest("test","test@example.com", "password123");
        when(repository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

        // Act
        userService.registerUser(request);

        // Assert
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar um usuário com email já cadastrado")
    void case02() {
        // Arrange
        UserRequest request = new UserRequest("test","test@example.com", "password123");
        when(repository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        // Act & Assert
        AlreadyExistException exception = assertThrows(AlreadyExistException.class, () -> userService.registerUser(request));
        assertEquals("Email já cadastrado", exception.getMessage());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve autenticar um usuário com sucesso")
    void case03() {
        // Arrange
        String email = "test@example.com";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(tokenService.generateToken(email)).thenReturn("generatedToken");

        LoginRequest request = new LoginRequest(email, rawPassword);

        // Act
        LoginResponse response = userService.signIn(request);

        // Assert
        assertEquals("generatedToken", response.token());
        assertEquals(email, response.email());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar autenticar um usuário com senha inválida")
    void case04() {
        // Arrange
        String email = "test@example.com";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        LoginRequest request = new LoginRequest(email, rawPassword);

        // Act & Assert
        CredentialsAccessException exception = assertThrows(CredentialsAccessException.class, () -> userService.signIn(request));
        assertEquals("Email ou senha inválidos", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar um usuário pelo email")
    void case05() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar retornar um usuário pelo email e não encontrar")
    void case06() {
        // Arrange
        String email = "notfound@example.com";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        CredentialsAccessException exception = assertThrows(CredentialsAccessException.class, () -> userService.getUserByEmail(email));
        assertEquals("Email ou senha inválidos", exception.getMessage());
    }

}