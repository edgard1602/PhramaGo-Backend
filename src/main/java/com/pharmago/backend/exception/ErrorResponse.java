package com.pharmago.backend.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String errorCode,
        String message,
        int status,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(
            String errorCode,
            String message,
            int status) {
        return new ErrorResponse(
                errorCode,
                message,
                status,
                LocalDateTime.now()
        );
    }
}