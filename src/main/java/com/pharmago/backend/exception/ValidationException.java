package com.pharmago.backend.exception;

public final class ValidationException
        extends BusinessException {

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}