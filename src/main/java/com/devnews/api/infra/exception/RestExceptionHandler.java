package com.devnews.api.infra.exception;

import com.devnews.api.domain.exception.AlreadyExistException;
import com.devnews.api.domain.exception.CredentialsAccessException;
import com.devnews.api.domain.exception.IllegalPermissionException;
import com.devnews.api.domain.exception.ResourceNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<List<DataValidation>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors()
                .stream()
                .map(DataValidation::new)
                .toList();

        log.warn("Erro de validação: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<String> handleBadCredentialsException() {
        log.warn("Tentativa de login com credenciais inválidas.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<String> handleAuthenticationException() {
        log.warn("Falha na autenticação.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<String> handleAccessDeniedException() {
        log.warn("Acesso negado.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado");
    }

    @ExceptionHandler(ResourceNotFound.class)
    protected ResponseEntity<String> handleResourceNotFoundException(ResourceNotFound ex) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AlreadyExistException.class)
    protected ResponseEntity<String> handleAlreadyExistException(AlreadyExistException ex) {
        log.warn("Recurso já existe: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(CredentialsAccessException.class)
    protected ResponseEntity<String> handleCredentialsAccessException(CredentialsAccessException ex) {
        log.warn("Acesso negado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalPermissionException.class)
    protected ResponseEntity<String> handleIllegalPermissionException(IllegalPermissionException ex) {
        log.warn("Permissão negada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleException(Exception ex) {
        log.error("Erro interno: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno");
    }

    private record DataValidation(String label, String msg) {
        public DataValidation(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

}
