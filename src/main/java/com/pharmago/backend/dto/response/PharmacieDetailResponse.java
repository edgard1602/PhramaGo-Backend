package com.pharmago.backend.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record PharmacieDetailResponse(
        Long id,
        String nom,
        String adresse,
        String quartier,
        String telephone,
        String whatsapp,
        Double latitude,
        Double longitude,
        Boolean isActive,
        Boolean isPartner,
        List<GardeResponse> gardes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}