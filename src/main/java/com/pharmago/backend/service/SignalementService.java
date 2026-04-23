package com.pharmago.backend.service;

import com.pharmago.backend.dto.request.SignalementRequest;
import com.pharmago.backend.dto.response.SignalementResponse;
import com.pharmago.backend.entity.Pharmacie;
import com.pharmago.backend.entity.Signalement;
import com.pharmago.backend.entity.StatutSignalement;
import com.pharmago.backend.exception.ResourceNotFoundException;
import com.pharmago.backend.mapper.SignalementMapper;
import com.pharmago.backend.repository.PharmacieRepository;
import com.pharmago.backend.repository.SignalementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignalementService {

    private final SignalementRepository signalementRepository;
    private final PharmacieRepository pharmacieRepository;
    private final SignalementMapper signalementMapper;

    // Endpoint public — utilisateur signale une erreur
    @Transactional
    public SignalementResponse creerSignalement(
            SignalementRequest request) {
        log.info("Nouveau signalement pour pharmacie id {}",
                request.pharmacieId());

        Pharmacie pharmacie = pharmacieRepository
                .findById(request.pharmacieId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pharmacie", request.pharmacieId()));

        Signalement signalement = Signalement.builder()
                .pharmacie(pharmacie)
                .typeErreur(request.typeErreur())
                .description(request.description())
                .build();

        return signalementMapper.toResponse(
                signalementRepository.save(signalement));
    }

    // Endpoint admin — voir tous les signalements en attente
    @Transactional(readOnly = true)
    public List<SignalementResponse> getSignalementsEnAttente() {
        return signalementRepository
                .findByStatutOrderByCreatedAtDesc(
                        StatutSignalement.EN_ATTENTE)
                .stream()
                .map(signalementMapper::toResponse)
                .toList();
    }

    // Endpoint admin — changer le statut d'un signalement
    @Transactional
    public SignalementResponse traiterSignalement(
            Long id, StatutSignalement nouveauStatut) {
        log.info("Traitement signalement id {} → {}",
                id, nouveauStatut);

        Signalement signalement = signalementRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Signalement", id));

        signalement.setStatut(nouveauStatut);
        return signalementMapper.toResponse(
                signalementRepository.save(signalement));
    }
}