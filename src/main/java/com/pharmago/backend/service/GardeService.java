package com.pharmago.backend.service;

import com.pharmago.backend.dto.request.GardeRequest;
import com.pharmago.backend.dto.response.GardeResponse;
import com.pharmago.backend.entity.Garde;
import com.pharmago.backend.entity.Pharmacie;
import com.pharmago.backend.exception.ConflictException;
import com.pharmago.backend.exception.ResourceNotFoundException;
import com.pharmago.backend.exception.ValidationException;
import com.pharmago.backend.mapper.GardeMapper;
import com.pharmago.backend.repository.GardeRepository;
import com.pharmago.backend.repository.PharmacieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GardeService {

    private final GardeRepository gardeRepository;
    private final PharmacieRepository pharmacieRepository;
    private final GardeMapper gardeMapper;

    @Transactional
    public GardeResponse creerGarde(GardeRequest request) {
        log.info("Création garde pour pharmacie id {}",
                request.pharmacieId());

        // Validation métier : date fin >= date début
        if (request.dateFin().isBefore(request.dateDebut())) {
            throw new ValidationException(
                    "La date de fin doit être après la date de début");
        }

        // Vérifier que la pharmacie existe
        Pharmacie pharmacie = pharmacieRepository
                .findById(request.pharmacieId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pharmacie", request.pharmacieId()));

        // Vérifier qu'il n'y a pas de conflit de dates
        boolean conflit = gardeRepository.existsGardeEnConflit(
                request.pharmacieId(),
                request.dateDebut(),
                request.dateFin()
        );

        if (conflit) {
            throw new ConflictException(
                    "Cette pharmacie a déjà une garde sur cette période");
        }

        Garde garde = Garde.builder()
                .pharmacie(pharmacie)
                .dateDebut(request.dateDebut())
                .dateFin(request.dateFin())
                .build();

        return gardeMapper.toResponse(gardeRepository.save(garde));
    }

    @Transactional(readOnly = true)
    public List<GardeResponse> getGardesByPharmacie(
            Long pharmacieId) {
        if (!pharmacieRepository.existsById(pharmacieId)) {
            throw new ResourceNotFoundException(
                    "Pharmacie", pharmacieId);
        }
        return gardeRepository
                .findByPharmacieIdOrderByDateDebutDesc(pharmacieId)
                .stream()
                .map(gardeMapper::toResponse)
                .toList();
    }

    @Transactional
    public void supprimerGarde(Long id) {
        log.info("Suppression garde id {}", id);
        if (!gardeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Garde", id);
        }
        gardeRepository.deleteById(id);
    }
}