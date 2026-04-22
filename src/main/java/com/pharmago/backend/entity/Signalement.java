package com.pharmago.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "signalements", indexes = {
        @Index(name = "idx_signalements_pharmacie",
                columnList = "pharmacie_id"),
        @Index(name = "idx_signalements_statut",
                columnList = "statut")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Signalement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La pharmacie est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacie_id", nullable = false)
    private Pharmacie pharmacie;

    @NotBlank(message = "Le type d'erreur est obligatoire")
    @Size(max = 50)
    @Column(name = "type_erreur", nullable = false)
    private String typeErreur;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutSignalement statut = StatutSignalement.EN_ATTENTE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}