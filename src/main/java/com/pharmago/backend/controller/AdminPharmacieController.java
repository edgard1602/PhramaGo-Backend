package com.pharmago.backend.controller;

import com.pharmago.backend.dto.request.PharmacieRequest;
import com.pharmago.backend.dto.response.PharmacieDetailResponse;
import com.pharmago.backend.service.PharmacieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/pharmacies")
@RequiredArgsConstructor
@Tag(name = "Admin - Pharmacies",
        description = "Endpoints admin pour la gestion des pharmacies")
public class AdminPharmacieController {

    private final PharmacieService pharmacieService;

    @PostMapping
    @Operation(summary = "Créer une pharmacie")
    public ResponseEntity<PharmacieDetailResponse> creerPharmacie(
            @Valid @RequestBody PharmacieRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pharmacieService.creerPharmacie(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une pharmacie")
    public ResponseEntity<PharmacieDetailResponse> modifierPharmacie(
            @PathVariable Long id,
            @Valid @RequestBody PharmacieRequest request) {

        return ResponseEntity.ok(
                pharmacieService.modifierPharmacie(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une pharmacie")
    public ResponseEntity<Void> supprimerPharmacie(
            @PathVariable Long id) {

        pharmacieService.supprimerPharmacie(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-actif")
    @Operation(summary = "Activer ou désactiver une pharmacie")
    public ResponseEntity<PharmacieDetailResponse> toggleActif(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                pharmacieService.toggleActif(id));
    }
}