package com.devnews.api.domain.exception;

public class IllegalPermissionException extends RuntimeException {
    public IllegalPermissionException(String message) {
        super(message);
    }
}
