package com.pharmago.backend.repository;

import com.pharmago.backend.entity.Signalement;
import com.pharmago.backend.entity.StatutSignalement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignalementRepository
        extends JpaRepository<Signalement, Long> {

    // Signalements par statut (pour l'admin)
    List<Signalement> findByStatutOrderByCreatedAtDesc(
            StatutSignalement statut
    );

    // Signalements d'une pharmacie spécifique
    List<Signalement> findByPharmacieIdOrderByCreatedAtDesc(
            Long pharmacieId
    );
}