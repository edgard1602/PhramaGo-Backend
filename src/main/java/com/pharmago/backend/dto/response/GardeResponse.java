package com.pharmago.backend.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GardeResponse(
        Long id,
        Long pharmacieId,
        String pharmacieNom,
        LocalDate dateDebut,
        LocalDate dateFin,
        LocalDateTime createdAt
) {}