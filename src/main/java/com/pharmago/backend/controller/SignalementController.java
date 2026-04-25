package com.pharmago.backend.controller;

import com.pharmago.backend.dto.request.SignalementRequest;
import com.pharmago.backend.dto.response.SignalementResponse;
import com.pharmago.backend.entity.StatutSignalement;
import com.pharmago.backend.service.SignalementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Signalements",
        description = "Gestion des signalements d'erreurs")
public class SignalementController {

    private final SignalementService signalementService;

    // Endpoint public — app mobile
    @PostMapping("/signalements")
    @Operation(summary = "Signaler une erreur sur une pharmacie")
    public ResponseEntity<SignalementResponse> creerSignalement(
            @Valid @RequestBody SignalementRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(signalementService.creerSignalement(request));
    }

    // Endpoints admin
    @GetMapping("/admin/signalements")
    @Operation(summary = "Signalements en attente")
    public ResponseEntity<List<SignalementResponse>> getSignalementsEnAttente() {

        return ResponseEntity.ok(
                signalementService.getSignalementsEnAttente());
    }

    @PatchMapping("/admin/signalements/{id}")
    @Operation(summary = "Traiter un signalement")
    public ResponseEntity<SignalementResponse> traiterSignalement(
            @PathVariable Long id,
            @RequestParam StatutSignalement statut) {

        return ResponseEntity.ok(
                signalementService.traiterSignalement(id, statut));
    }
}