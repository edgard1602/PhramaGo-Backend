package com.pharmago.backend.exception;

public final class ResourceNotFoundException
        extends BusinessException {

    public ResourceNotFoundException(String resource, Long id) {
        super(
                "%s avec l'id %d introuvable".formatted(resource, id),
                "RESOURCE_NOT_FOUND"
        );
    }

    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}