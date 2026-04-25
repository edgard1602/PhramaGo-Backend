package com.pharmago.backend.dto.request;

import jakarta.validation.constraints.*;

public record SignalementRequest(

        @NotNull(message = "L'id de la pharmacie est obligatoire")
        Long pharmacieId,

        @NotBlank(message = "Le type d'erreur est obligatoire")
        @Size(max = 50, message = "Le type d'erreur ne peut pas dépasser 50 caractères")
        String typeErreur,

        @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
        String description
) {}