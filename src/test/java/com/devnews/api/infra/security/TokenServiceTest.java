package com.devnews.api.infra.security;

import com.devnews.api.domain.exception.IllegalPermissionException;
import com.devnews.api.domain.exception.TokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(tokenService, "token", "testSecretKey");
    }

    @Test
    @DisplayName("Gera token com sucesso")
    void case01() {
        // Arrange
        String email = "test@example.com";

        // Act
        String generatedToken = tokenService.generateToken(email);

        // Assert
        assertNotNull(generatedToken);
        assertFalse(generatedToken.isEmpty());
    }

    @Test
    @DisplayName("Falha ao gerar token")
    void case02() {
        // Arrange
        ReflectionTestUtils.setField(tokenService, "token", null); // Set secret to null to simulate failure
        String email = "test@example.com";

        // Act & Assert
        TokenException exception = assertThrows(TokenException.class, () -> {
            tokenService.generateToken(email);
        });

        assertEquals("Chave secreta não configurada", exception.getMessage());
    }

    @Test
    @DisplayName("Token válido com sucesso")
    void case03() {
        // Arrange
        String email = "test@example.com";
        String generatedToken = tokenService.generateToken(email);

        // Act
        String subject = tokenService.validateToken(generatedToken);

        // Assert
        assertNotNull(subject);
        assertEquals(email, subject);
    }

    @Test
    @DisplayName("Token inválido")
    void case04() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tokenService.validateToken(invalidToken);
        });

        assertTrue(exception.getMessage().contains("Token inválido"));
    }

    @Test
    @DisplayName("Recupera email do usuário logado com sucesso")
    void case05() {
        // Arrange
        String email = "logged@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);

        // Act
        String loggedEmail = tokenService.recoverLoggedEmail();

        // Assert
        assertEquals(email, loggedEmail, "O email recuperado deve ser igual ao do usuário logado");
    }

    @Test
    @DisplayName("Falha ao recuperar email do usuário logado")
    void case06() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("anonymousUser");
        SecurityContextHolder.setContext(securityContext);

        // Act & Assert
        IllegalPermissionException exception = assertThrows(IllegalPermissionException.class,
                () -> tokenService.recoverLoggedEmail());
        assertEquals("Você não está logado para realizar esta ação.", exception.getMessage());
    }

}