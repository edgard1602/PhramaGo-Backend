package com.pharmago.backend.repository;

import com.pharmago.backend.AbstractContainerIT;
import com.pharmago.backend.entity.Garde;
import com.pharmago.backend.entity.Pharmacie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace =
        AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Tests repository - Pharmacie")
class PharmacieRepositoryTest extends AbstractContainerIT {

    @Autowired
    private PharmacieRepository pharmacieRepository;

    @Autowired
    private GardeRepository gardeRepository;

    private Pharmacie pharmacie;

    @BeforeEach
    void setUp() {
        gardeRepository.deleteAll();
        pharmacieRepository.deleteAll();

        pharmacie = pharmacieRepository.save(
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
    }

    @Test
    @DisplayName("Doit trouver les pharmacies en garde aujourd'hui")
    void findPharmaciesEnGarde_gardeActive_retournePharmacie() {
        gardeRepository.save(
                Garde.builder()
                        .pharmacie(pharmacie)
                        .dateDebut(LocalDate.now().minusDays(1))
                        .dateFin(LocalDate.now().plusDays(1))
                        .build()
        );

        List<Pharmacie> result =
                pharmacieRepository.findPharmaciesEnGarde(LocalDate.now());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom())
                .isEqualTo("Pharmacie Centrale");
    }

    @Test
    @DisplayName("Ne doit pas retourner une pharmacie sans garde active")
    void findPharmaciesEnGarde_sansGarde_retourneListeVide() {
        List<Pharmacie> result =
                pharmacieRepository.findPharmaciesEnGarde(LocalDate.now());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Ne doit pas retourner une pharmacie inactive")
    void findPharmaciesEnGarde_pharmacieInactive_retourneListeVide() {
        pharmacie.setIsActive(false);
        pharmacieRepository.save(pharmacie);

        gardeRepository.save(
                Garde.builder()
                        .pharmacie(pharmacie)
                        .dateDebut(LocalDate.now().minusDays(1))
                        .dateFin(LocalDate.now().plusDays(1))
                        .build()
        );

        List<Pharmacie> result =
                pharmacieRepository.findPharmaciesEnGarde(LocalDate.now());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Doit filtrer par quartier correctement")
    void findPharmaciesEnGardeByQuartier_quartierCorrect_retournePharmacie() {
        gardeRepository.save(
                Garde.builder()
                        .pharmacie(pharmacie)
                        .dateDebut(LocalDate.now().minusDays(1))
                        .dateFin(LocalDate.now().plusDays(1))
                        .build()
        );

        List<Pharmacie> result =
                pharmacieRepository.findPharmaciesEnGardeByQuartier(
                        LocalDate.now(), "Hédzranawoé");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Doit retourner liste vide pour mauvais quartier")
    void findPharmaciesEnGardeByQuartier_mauvaisQuartier_retourneListeVide() {
        gardeRepository.save(
                Garde.builder()
                        .pharmacie(pharmacie)
                        .dateDebut(LocalDate.now().minusDays(1))
                        .dateFin(LocalDate.now().plusDays(1))
                        .build()
        );

        List<Pharmacie> result =
                pharmacieRepository.findPharmaciesEnGardeByQuartier(
                        LocalDate.now(), "Adidogomé");

        assertThat(result).isEmpty();
    }
}