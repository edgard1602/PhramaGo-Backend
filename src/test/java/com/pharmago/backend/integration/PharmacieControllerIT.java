package com.pharmago.backend.integration;

import com.pharmago.backend.AbstractContainerIT;
import com.pharmago.backend.dto.request.PharmacieRequest;
import com.pharmago.backend.entity.Garde;
import com.pharmago.backend.entity.Pharmacie;
import com.pharmago.backend.repository.GardeRepository;
import com.pharmago.backend.repository.PharmacieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment =
        SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@DisplayName("Tests intégration - PharmacieController")
class PharmacieControllerIT extends AbstractContainerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PharmacieRepository pharmacieRepository;

    @Autowired
    private GardeRepository gardeRepository;

    private static final String API_KEY = "pharmago-dev-key-2026";

    @BeforeEach
    void setUp() {
        gardeRepository.deleteAll();
        pharmacieRepository.deleteAll();
    }

    private HttpHeaders headersAvecApiKey() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    @DisplayName("GET /garde doit retourner liste vide si aucune garde")
    void getPharmaciesDeGarde_sansGarde_retourneListeVide() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/v1/pharmacies/garde", String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");
    }

    @Test
    @DisplayName("GET /garde doit retourner les pharmacies actives")
    void getPharmaciesDeGarde_avecGarde_retournePharmacie() {
        Pharmacie pharmacie = pharmacieRepository.save(
                Pharmacie.builder()
                        .nom("Pharmacie Centrale")
                        .quartier("Hédzranawoé")
                        .telephone("+22890000001")
                        .latitude(6.1375)
                        .longitude(1.2123)
                        .isActive(true)
                        .isPartner(false)
                        .build()
        );

        gardeRepository.save(
                Garde.builder()
                        .pharmacie(pharmacie)
                        .dateDebut(LocalDate.now().minusDays(1))
                        .dateFin(LocalDate.now().plusDays(1))
                        .build()
        );

        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/v1/pharmacies/garde", String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .contains("Pharmacie Centrale");
    }

    @Test
    @DisplayName("POST /admin/pharmacies doit créer une pharmacie")
    void creerPharmacie_requestValide_retourne201() {
        PharmacieRequest request = new PharmacieRequest(
                "Nouvelle Pharmacie", "Adresse test",
                "Adidogomé", "+22890000002", null,
                6.1400, 1.2200
        );

        HttpEntity<PharmacieRequest> entity =
                new HttpEntity<>(request, headersAvecApiKey());

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/admin/pharmacies",
                HttpMethod.POST,
                entity,
                String.class
        );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody())
                .contains("Nouvelle Pharmacie");
    }

    @Test
    @DisplayName("POST /admin/pharmacies sans API Key doit retourner 401")
    void creerPharmacie_sansApiKey_retourne401() {
        PharmacieRequest request = new PharmacieRequest(
                "Pharmacie Test", null, null,
                null, null, 6.1400, 1.2200
        );

        HttpEntity<PharmacieRequest> entity =
                new HttpEntity<>(request, new HttpHeaders());

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/admin/pharmacies",
                HttpMethod.POST,
                entity,
                String.class
        );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("GET /pharmacies/{id} inexistant doit retourner 404")
    void getPharmacieById_idInexistant_retourne404() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/v1/pharmacies/999", String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
                .contains("RESOURCE_NOT_FOUND");
    }
}