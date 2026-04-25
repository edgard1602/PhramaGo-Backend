package com.pharmago.backend.controller;

import com.pharmago.backend.dto.response.PharmacieDetailResponse;
import com.pharmago.backend.dto.response.PharmacieResponse;
import com.pharmago.backend.service.PharmacieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pharmacies")
@RequiredArgsConstructor
@Tag(name = "Pharmacies", description = "Endpoints publics pour l'app mobile")
public class PublicPharmacieController {

    private final PharmacieService pharmacieService;

    @GetMapping("/garde")
    @Operation(summary = "Pharmacies de garde actives",
            description = "Retourne les pharmacies de garde " +
                    "triées par distance si géoloc fournie, " +
                    "sinon filtrées par quartier")
    public ResponseEntity<List<PharmacieResponse>> getPharmaciesDeGarde(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) String quartier) {

        return ResponseEntity.ok(
                pharmacieService.getPharmaciesDeGarde(
                        latitude, longitude, quartier));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'une pharmacie")
    public ResponseEntity<PharmacieDetailResponse> getPharmacieById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                pharmacieService.getPharmacieById(id));
    }

    @GetMapping("/quartiers")
    @Operation(summary = "Liste des quartiers disponibles")
    public ResponseEntity<List<String>> getQuartiers() {
        return ResponseEntity.ok(pharmacieService.getQuartiers());
    }
}