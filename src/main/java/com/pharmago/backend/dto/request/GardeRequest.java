package com.pharmago.backend.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record GardeRequest(

        @NotNull(message = "L'id de la pharmacie est obligatoire")
        Long pharmacieId,

        @NotNull(message = "La date de début est obligatoire")
        @FutureOrPresent(message = "La date de début doit être aujourd'hui ou dans le futur")
        LocalDate dateDebut,

        @NotNull(message = "La date de fin est obligatoire")
        @FutureOrPresent(message = "La date de fin doit être aujourd'hui ou dans le futur")
        LocalDate dateFin
) {}