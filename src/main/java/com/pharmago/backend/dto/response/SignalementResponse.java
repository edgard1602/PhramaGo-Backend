package com.pharmago.backend.dto.response;

import com.pharmago.backend.entity.StatutSignalement;

import java.time.LocalDateTime;

public record SignalementResponse(
        Long id,
        Long pharmacieId,
        String pharmacieNom,
        String typeErreur,
        String description,
        StatutSignalement statut,
        LocalDateTime createdAt
) {}