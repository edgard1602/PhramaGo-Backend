package com.pharmago.backend.service;

import com.pharmago.backend.dto.request.PharmacieRequest;
import com.pharmago.backend.dto.response.PharmacieDetailResponse;
import com.pharmago.backend.dto.response.PharmacieResponse;
import com.pharmago.backend.entity.Pharmacie;
import com.pharmago.backend.exception.ResourceNotFoundException;
import com.pharmago.backend.mapper.PharmacieMapper;
import com.pharmago.backend.repository.PharmacieRepository;
import com.pharmago.backend.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacieService {

    private final PharmacieRepository pharmacieRepository;
    private final PharmacieMapper pharmacieMapper;

    // ─── Endpoints publics (app mobile) ───────────────────────

    @Transactional(readOnly = true)
    public List<PharmacieResponse> getPharmaciesDeGarde(
            Double latitude,
            Double longitude,
            String quartier) {

        LocalDate today = LocalDate.now();
        log.debug("Recherche pharmacies de garde pour le {}", today);

        List<Pharmacie> pharmacies;

        // Filtre par quartier si fourni
        if (quartier != null && !quartier.isBlank()) {
            pharmacies = pharmacieRepository
                    .findPharmaciesEnGardeByQuartier(today, quartier);
        } else {
            pharmacies = pharmacieRepository
                    .findPharmaciesEnGarde(today);
        }

        // Mapping + calcul distance si géoloc disponible
        List<PharmacieResponse> responses = pharmacies.stream()
                .map(p -> {
                    PharmacieResponse response =
                            pharmacieMapper.toResponse(p);

                    if (latitude != null && longitude != null) {
                        double distance = GeoUtils.arrondir(
                                GeoUtils.calculerDistance(
                                        latitude, longitude,
                                        p.getLatitude(), p.getLongitude()
                                )
                        );
                        // On reconstruit le record avec la distance
                        return new PharmacieResponse(
                                response.id(),
                                response.nom(),
                                response.quartier(),
                                response.telephone(),
                                response.whatsapp(),
                                response.latitude(),
                                response.longitude(),
                                response.isActive(),
                                response.isPartner(),
                                distance
                        );
                    }
                    return response;
                })
                .toList();

        // Tri par distance si géoloc disponible
        if (latitude != null && longitude != null) {
            return responses.stream()
                    .sorted((a, b) ->
                            Double.compare(a.distance(), b.distance()))
                    .toList();
        }

        return responses;
    }

    @Transactional(readOnly = true)
    public PharmacieDetailResponse getPharmacieById(Long id) {
        log.debug("Recherche pharmacie avec id {}", id);
        return pharmacieRepository.findById(id)
                .map(pharmacieMapper::toDetailResponse)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pharmacie", id));
    }

    @Transactional(readOnly = true)
    public List<String> getQuartiers() {
        return pharmacieRepository.findAllQuartiers();
    }

    // ─── Endpoints admin (back-office) ────────────────────────

    @Transactional
    public PharmacieDetailResponse creerPharmacie(
            PharmacieRequest request) {
        log.info("Création pharmacie : {}", request.nom());
        Pharmacie pharmacie = pharmacieMapper.toEntity(request);
        Pharmacie saved = pharmacieRepository.save(pharmacie);
        return pharmacieMapper.toDetailResponse(saved);
    }

    @Transactional
    public PharmacieDetailResponse modifierPharmacie(
            Long id, PharmacieRequest request) {
        log.info("Modification pharmacie id {}", id);
        Pharmacie pharmacie = pharmacieRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pharmacie", id));

        pharmacieMapper.updateEntityFromRequest(request, pharmacie);
        Pharmacie saved = pharmacieRepository.save(pharmacie);
        return pharmacieMapper.toDetailResponse(saved);
    }

    @Transactional
    public void supprimerPharmacie(Long id) {
        log.info("Suppression pharmacie id {}", id);
        if (!pharmacieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pharmacie", id);
        }
        pharmacieRepository.deleteById(id);
    }

    @Transactional
    public PharmacieDetailResponse toggleActif(Long id) {
        log.info("Toggle actif pharmacie id {}", id);
        Pharmacie pharmacie = pharmacieRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pharmacie", id));

        pharmacie.setIsActive(!pharmacie.getIsActive());
        return pharmacieMapper.toDetailResponse(
                pharmacieRepository.save(pharmacie));
    }
}