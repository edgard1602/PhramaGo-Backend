package com.pharmago.backend.dto.request;

import jakarta.validation.constraints.*;

public record PharmacieRequest(

        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 255, message = "Le nom ne peut pas dépasser 255 caractères")
        String nom,

        String adresse,

        @Size(max = 100, message = "Le quartier ne peut pas dépasser 100 caractères")
        String quartier,

        @Size(max = 20, message = "Le téléphone ne peut pas dépasser 20 caractères")
        String telephone,

        @Size(max = 20, message = "Le WhatsApp ne peut pas dépasser 20 caractères")
        String whatsapp,

        @NotNull(message = "La latitude est obligatoire")
        @DecimalMin(value = "-90.0", message = "Latitude invalide")
        @DecimalMax(value = "90.0", message = "Latitude invalide")
        Double latitude,

        @NotNull(message = "La longitude est obligatoire")
        @DecimalMin(value = "-180.0", message = "Longitude invalide")
        @DecimalMax(value = "180.0", message = "Longitude invalide")
        Double longitude
) {}