package com.pharmago.backend.controller;

import com.pharmago.backend.dto.request.GardeRequest;
import com.pharmago.backend.dto.response.GardeResponse;
import com.pharmago.backend.service.GardeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/gardes")
@RequiredArgsConstructor
@Tag(name = "Admin - Gardes",
        description = "Endpoints admin pour la gestion des gardes")
public class AdminGardeController {

    private final GardeService gardeService;

    @PostMapping
    @Operation(summary = "Créer une garde")
    public ResponseEntity<GardeResponse> creerGarde(
            @Valid @RequestBody GardeRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(gardeService.creerGarde(request));
    }

    @GetMapping("/pharmacie/{pharmacieId}")
    @Operation(summary = "Gardes d'une pharmacie")
    public ResponseEntity<List<GardeResponse>> getGardesByPharmacie(
            @PathVariable Long pharmacieId) {

        return ResponseEntity.ok(
                gardeService.getGardesByPharmacie(pharmacieId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une garde")
    public ResponseEntity<Void> supprimerGarde(
            @PathVariable Long id) {

        gardeService.supprimerGarde(id);
        return ResponseEntity.noContent().build();
    }
}