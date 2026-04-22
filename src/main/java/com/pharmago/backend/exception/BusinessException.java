package com.pharmago.backend.exception;

public sealed class BusinessException extends RuntimeException
        permits ResourceNotFoundException,
        ValidationException,
        ConflictException {

    private final String errorCode;

    protected BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}