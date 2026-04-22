package com.pharmago.backend.repository;

import com.pharmago.backend.entity.Pharmacie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PharmacieRepository
        extends JpaRepository<Pharmacie, Long> {

    // Toutes les pharmacies de garde à une date donnée
    @Query("""
        SELECT DISTINCT p FROM Pharmacie p
        JOIN p.gardes g
        WHERE g.dateDebut <= :date
        AND g.dateFin >= :date
        AND p.isActive = true
        ORDER BY p.nom
        """)
    List<Pharmacie> findPharmaciesEnGarde(
            @Param("date") LocalDate date
    );

    // Pharmacies de garde filtrées par quartier
    @Query("""
        SELECT DISTINCT p FROM Pharmacie p
        JOIN p.gardes g
        WHERE g.dateDebut <= :date
        AND g.dateFin >= :date
        AND p.isActive = true
        AND LOWER(p.quartier) = LOWER(:quartier)
        ORDER BY p.nom
        """)
    List<Pharmacie> findPharmaciesEnGardeByQuartier(
            @Param("date") LocalDate date,
            @Param("quartier") String quartier
    );

    // Liste des quartiers distincts disponibles
    @Query("""
        SELECT DISTINCT p.quartier FROM Pharmacie p
        WHERE p.isActive = true
        AND p.quartier IS NOT NULL
        ORDER BY p.quartier
        """)
    List<String> findAllQuartiers();
}