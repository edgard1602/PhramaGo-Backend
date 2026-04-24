package com.pharmago.backend.service;

import com.pharmago.backend.dto.request.PharmacieRequest;
import com.pharmago.backend.dto.response.PharmacieDetailResponse;
import com.pharmago.backend.dto.response.PharmacieResponse;
import com.pharmago.backend.entity.Pharmacie;
import com.pharmago.backend.exception.ResourceNotFoundException;
import com.pharmago.backend.mapper.PharmacieMapper;
import com.pharmago.backend.repository.PharmacieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - PharmacieService")
class PharmacieServiceTest {

    // Les @Mock créent des faux objets
    // qui simulent le comportement réel
    @Mock
    private PharmacieRepository pharmacieRepository;

    @Mock
    private PharmacieMapper pharmacieMapper;

    // @InjectMocks crée le vrai service
    // et injecte les mocks dedans
    @InjectMocks
    private PharmacieService pharmacieService;

    private Pharmacie pharmacie;
    private PharmacieResponse pharmacieResponse;
    private PharmacieDetailResponse pharmacieDetailResponse;

    @BeforeEach
    void setUp() {
        // Données de test réutilisées dans chaque test
        pharmacie = Pharmacie.builder()
                .id(1L)
                .nom("Pharmacie Centrale")
                .quartier("Hédzranawoé")
                .telephone("+22890000001")
                .latitude(6.1375)
                .longitude(1.2123)
                .isActive(true)
                .isPartner(false)
                .build();

        pharmacieResponse = new PharmacieResponse(
                1L, "Pharmacie Centrale", "Hédzranawoé",
                "+22890000001", null, 6.1375, 1.2123,
                true, false, null
        );

        pharmacieDetailResponse = new PharmacieDetailResponse(
                1L, "Pharmacie Centrale", null, "Hédzranawoé",
                "+22890000001", null, 6.1375, 1.2123,
                true, false, List.of(), null, null
        );
    }

    @Test
    @DisplayName("Doit retourner les pharmacies de garde sans géoloc")
    void getPharmaciesDeGarde_sansGeoloc_retourneListeSimple() {
        // GIVEN
        when(pharmacieRepository.findPharmaciesEnGarde(
                any(LocalDate.class)))
                .thenReturn(List.of(pharmacie));

        when(pharmacieMapper.toResponse(pharmacie))
                .thenReturn(pharmacieResponse);

        // WHEN
        List<PharmacieResponse> result =
                pharmacieService.getPharmaciesDeGarde(
                        null, null, null);

        // THEN
        assertThat(result).hasSize(1);
        assertThat(result.get(0).nom())
                .isEqualTo("Pharmacie Centrale");
        assertThat(result.get(0).distance()).isNull();

        // Vérifie que le repository a bien été appelé
        verify(pharmacieRepository)
                .findPharmaciesEnGarde(any(LocalDate.class));
    }

    @Test
    @DisplayName("Doit retourner les pharmacies triées par distance")
    void getPharmaciesDeGarde_avecGeoloc_triParDistance() {
        // GIVEN
        Pharmacie pharmacie2 = Pharmacie.builder()
                .id(2L)
                .nom("Pharmacie du Port")
                .latitude(6.1300) // Plus proche
                .longitude(1.2100)
                .isActive(true)
                .build();

        PharmacieResponse response2 = new PharmacieResponse(
                2L, "Pharmacie du Port", null,
                null, null, 6.1300, 1.2100,
                true, false, null
        );

        when(pharmacieRepository.findPharmaciesEnGarde(
                any(LocalDate.class)))
                .thenReturn(List.of(pharmacie, pharmacie2));

        when(pharmacieMapper.toResponse(pharmacie))
                .thenReturn(pharmacieResponse);
        when(pharmacieMapper.toResponse(pharmacie2))
                .thenReturn(response2);

        // Position utilisateur proche de pharmacie2
        double userLat = 6.1295;
        double userLon = 1.2095;

        // WHEN
        List<PharmacieResponse> result =
                pharmacieService.getPharmaciesDeGarde(
                        userLat, userLon, null);

        // THEN
        assertThat(result).hasSize(2);
        // La première doit être la plus proche
        assertThat(result.get(0).distance())
                .isLessThan(result.get(1).distance());
    }

    @Test
    @DisplayName("Doit retourner une pharmacie par son id")
    void getPharmacieById_idExistant_retournePharmacie() {
        // GIVEN
        when(pharmacieRepository.findById(1L))
                .thenReturn(Optional.of(pharmacie));
        when(pharmacieMapper.toDetailResponse(pharmacie))
                .thenReturn(pharmacieDetailResponse);

        // WHEN
        PharmacieDetailResponse result =
                pharmacieService.getPharmacieById(1L);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.nom())
                .isEqualTo("Pharmacie Centrale");
    }

    @Test
    @DisplayName("Doit lancer une exception si pharmacie introuvable")
    void getPharmacieById_idInexistant_lanceException() {
        // GIVEN
        when(pharmacieRepository.findById(99L))
                .thenReturn(Optional.empty());

        // WHEN + THEN
        assertThatThrownBy(() ->
                pharmacieService.getPharmacieById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Doit créer une pharmacie correctement")
    void creerPharmacie_requestValide_retournePharmacie() {
        // GIVEN
        PharmacieRequest request = new PharmacieRequest(
                "Nouvelle Pharmacie", "Adresse test",
                "Adidogomé", "+22890000002", null,
                6.1400, 1.2200
        );

        when(pharmacieMapper.toEntity(request))
                .thenReturn(pharmacie);
        when(pharmacieRepository.save(pharmacie))
                .thenReturn(pharmacie);
        when(pharmacieMapper.toDetailResponse(pharmacie))
                .thenReturn(pharmacieDetailResponse);

        // WHEN
        PharmacieDetailResponse result =
                pharmacieService.creerPharmacie(request);

        // THEN
        assertThat(result).isNotNull();
        verify(pharmacieRepository).save(any(Pharmacie.class));
    }

    @Test
    @DisplayName("Doit lancer une exception à la suppression si introuvable")
    void supprimerPharmacie_idInexistant_lanceException() {
        // GIVEN
        when(pharmacieRepository.existsById(99L))
                .thenReturn(false);

        // WHEN + THEN
        assertThatThrownBy(() ->
                pharmacieService.supprimerPharmacie(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        // Vérifie que deleteById n'a jamais été appelé
        verify(pharmacieRepository, never())
                .deleteById(any());
    }
}