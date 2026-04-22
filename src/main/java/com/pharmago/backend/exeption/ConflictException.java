package com.pharmago.backend.exception;

public final class ConflictException
        extends com.pharmago.backend.exception.BusinessException {

    public ConflictException(String message) {
        super(message, "CONFLICT_ERROR");
    }
}