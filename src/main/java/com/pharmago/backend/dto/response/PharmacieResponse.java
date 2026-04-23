package com.pharmago.backend.dto.response;

public record PharmacieResponse(
        Long id,
        String nom,
        String quartier,
        String telephone,
        String whatsapp,
        Double latitude,
        Double longitude,
        Boolean isActive,
        Boolean isPartner,
        Double distance
) {}