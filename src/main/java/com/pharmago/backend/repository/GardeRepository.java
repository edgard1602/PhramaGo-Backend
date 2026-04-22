package com.pharmago.backend.repository;

import com.pharmago.backend.entity.Garde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GardeRepository
        extends JpaRepository<Garde, Long> {

    // Vérifier si une pharmacie est déjà en garde
    // sur une période donnée (éviter les doublons)
    @Query("""
        SELECT COUNT(g) > 0 FROM Garde g
        WHERE g.pharmacie.id = :pharmacieId
        AND g.dateDebut <= :dateFin
        AND g.dateFin >= :dateDebut
        """)
    boolean existsGardeEnConflit(
            @Param("pharmacieId") Long pharmacieId,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );

    // Gardes d'une pharmacie spécifique
    List<Garde> findByPharmacieIdOrderByDateDebutDesc(
            Long pharmacieId
    );
}