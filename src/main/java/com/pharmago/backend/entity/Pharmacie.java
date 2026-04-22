package com.pharmago.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pharmacies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pharmacie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 255)
    @Column(nullable = false)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String adresse;

    @Size(max = 100)
    @Column(name = "quartier")
    private String quartier;

    @Size(max = 20)
    @Column(name = "telephone")
    private String telephone;

    @Size(max = 20)
    @Column(name = "whatsapp")
    private String whatsapp;

    @NotNull(message = "La latitude est obligatoire")
    @DecimalMin(value = "-90.0", message = "Latitude invalide")
    @DecimalMax(value = "90.0", message = "Latitude invalide")
    @Column(nullable = false, precision = 10, scale = 8)
    private Double latitude;

    @NotNull(message = "La longitude est obligatoire")
    @DecimalMin(value = "-180.0", message = "Longitude invalide")
    @DecimalMax(value = "180.0", message = "Longitude invalide")
    @Column(nullable = false, precision = 11, scale = 8)
    private Double longitude;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "is_partner")
    @Builder.Default
    private Boolean isPartner = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "pharmacie",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<Garde> gardes = new ArrayList<>();
}