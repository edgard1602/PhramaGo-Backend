package com.pharmago.backend.service;

import com.pharmago.backend.config.VillesConfig;
import com.pharmago.backend.dto.request.ImportGooglePlacesRequest;
import com.pharmago.backend.dto.response.GooglePlacesResponse;
import com.pharmago.backend.dto.response.ImportResultResponse;
import com.pharmago.backend.entity.Pharmacie;
import com.pharmago.backend.exception.ValidationException;
import com.pharmago.backend.repository.PharmacieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GooglePlacesService {

    private final PharmacieRepository pharmacieRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${google.places.api-key}")
    private String apiKey;

    private static final String PLACES_NEW_URL =
            "https://places.googleapis.com/v1/places:searchNearby";

    // ─── Import ville complète ─────────────────────────────────

    @Transactional
    public ImportResultResponse importerVille(String ville) {
        List<VillesConfig.VilleConfig> zones =
                VillesConfig.VILLES_ZONES.get(ville);

        if (zones == null) {
            throw new ValidationException("Ville non supportée : " + ville);
        }

        int totalImportees = 0;
        int totalDoublons = 0;
        List<String> toutesImportees = new ArrayList<>();

        for (VillesConfig.VilleConfig zone : zones) {
            log.info("Import zone : {}", zone.nom());

            ImportResultResponse result = importerZone(
                    zone.latitude(),
                    zone.longitude(),
                    zone.rayon(),
                    ville
            );

            totalImportees += result.importees();
            totalDoublons += result.doublons();
            toutesImportees.addAll(result.pharmaciesImportees());

            // Pause entre les requêtes
            try { Thread.sleep(1000); }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return new ImportResultResponse(
                totalImportees + totalDoublons,
                totalImportees,
                totalDoublons,
                0,
                toutesImportees,
                List.of()
        );
    }

    // ─── Import par zone géographique ─────────────────────────

    @Transactional
    public ImportResultResponse importerQuartier(
            ImportGooglePlacesRequest request) {
        return importerZone(
                request.latitude(),
                request.longitude(),
                request.rayon(),
                request.quartier()
        );
    }

    // ─── Logique commune d'import ──────────────────────────────

    private ImportResultResponse importerZone(
            Double latitude,
            Double longitude,
            Integer rayon,
            String quartier) {

        log.info("Import zone — lat:{} lon:{} rayon:{}m",
                latitude, longitude, rayon);

        List<GooglePlacesResponse.GooglePlaceResult> resultats =
                appellerPlacesNewApi(latitude, longitude, rayon);

        log.info("{} pharmacies trouvées", resultats.size());

        int importees = 0;
        int doublons = 0;
        int erreurs = 0;
        List<String> nomImportees = new ArrayList<>();
        List<String> erreurDetails = new ArrayList<>();

        for (GooglePlacesResponse.GooglePlaceResult result
                : resultats) {
            try {
                if (verifierDoublon(result)) {
                    doublons++;
                    continue;
                }

                // Numéro de téléphone — international si disponible
                String telephone =
                        result.internationalPhoneNumber() != null
                                ? result.internationalPhoneNumber()
                                : result.nationalPhoneNumber();

                Pharmacie pharmacie = Pharmacie.builder()
                        .nom(result.displayName().text())
                        .adresse(result.formattedAddress())
                        .quartier(quartier)
                        .telephone(telephone)
                        .whatsapp(telephone)
                        .latitude(result.location().latitude())
                        .longitude(result.location().longitude())
                        .isActive(true)
                        .isPartner(false)
                        .build();

                pharmacieRepository.save(pharmacie);
                importees++;
                nomImportees.add(result.displayName().text());
                log.info("✅ {}", result.displayName().text());

            } catch (Exception e) {
                erreurs++;
                erreurDetails.add(
                        result.displayName().text() +
                                " → " + e.getMessage());
                log.error("❌ {} : {}",
                        result.displayName().text(),
                        e.getMessage());
            }
        }

        return new ImportResultResponse(
                resultats.size(),
                importees,
                doublons,
                erreurs,
                nomImportees,
                erreurDetails
        );
    }

    // ─── Appel Places API New ──────────────────────────────────

    private List<GooglePlacesResponse.GooglePlaceResult>
    appellerPlacesNewApi(
            Double latitude,
            Double longitude,
            Integer rayon) {

        // Corps de la requête POST
        Map<String, Object> requestBody = Map.of(
                "includedTypes", List.of("pharmacy"),
                "maxResultCount", 20,
                "locationRestriction", Map.of(
                        "circle", Map.of(
                                "center", Map.of(
                                        "latitude", latitude,
                                        "longitude", longitude
                                ),
                                "radius", rayon.doubleValue()
                        )
                )
        );

        // FieldMask — on demande exactement ce dont on a besoin
        String fieldMask = String.join(",",
                "places.id",
                "places.displayName",
                "places.formattedAddress",
                "places.nationalPhoneNumber",
                "places.internationalPhoneNumber",
                "places.location"
        );

        GooglePlacesResponse response = webClientBuilder
                .build()
                .post()
                .uri(PLACES_NEW_URL)
                .header("X-Goog-Api-Key", apiKey)
                .header("X-Goog-FieldMask", fieldMask)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GooglePlacesResponse.class)
                .block();

        if (response == null || response.places() == null) {
            log.warn("Aucun résultat Places API");
            return List.of();
        }

        return response.places();
    }

    // ─── Vérification doublon ──────────────────────────────────

    private boolean verifierDoublon(
            GooglePlacesResponse.GooglePlaceResult result) {

        return pharmacieRepository.findAll().stream()
                .anyMatch(p ->
                        p.getNom().equalsIgnoreCase(
                                result.displayName().text()) ||
                                (Math.abs(p.getLatitude() -
                                        result.location().latitude()) < 0.001 &&
                                        Math.abs(p.getLongitude() -
                                                result.location().longitude()) < 0.001)
                );
    }
}