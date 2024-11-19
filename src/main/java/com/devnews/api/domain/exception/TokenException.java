package com.devnews.api.domain.exception;

public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}
