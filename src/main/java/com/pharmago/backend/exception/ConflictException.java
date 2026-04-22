package com.pharmago.backend.exception;

public final class ConflictException
        extends BusinessException {

    public ConflictException(String message) {
        super(message, "CONFLICT_ERROR");
    }
}