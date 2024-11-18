package com.devnews.api.domain.exception;

public class CredentialsAccessException extends RuntimeException {
    public CredentialsAccessException(String message) {
        super(message);
    }
}
