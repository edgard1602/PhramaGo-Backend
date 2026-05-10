package com.pharmago.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ImportGooglePlacesRequest(

        @NotNull(message = "La latitude est obligatoire")
        Double latitude,

        @NotNull(message = "La longitude est obligatoire")
        Double longitude,

        @NotNull(message = "Le rayon est obligatoire")
        @Positive(message = "Le rayon doit être positif")
        Integer rayon,

        @NotBlank(message = "Le quartier est obligatoire")
        String quartier
) {}