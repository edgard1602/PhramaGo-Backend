package com.pharmago.backend.mapper;

import com.pharmago.backend.dto.request.PharmacieRequest;
import com.pharmago.backend.dto.response.PharmacieDetailResponse;
import com.pharmago.backend.dto.response.PharmacieResponse;
import com.pharmago.backend.entity.Pharmacie;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PharmacieMapper {

    // Entity → PharmacieResponse (sans distance)
    @Mapping(target = "distance", ignore = true)
    PharmacieResponse toResponse(Pharmacie pharmacie);

    // Entity → PharmacieDetailResponse
    PharmacieDetailResponse toDetailResponse(Pharmacie pharmacie);

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "gardes", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "isPartner", constant = "false")
    Pharmacie toEntity(PharmacieRequest request);

    // Mise à jour d'une entité existante
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "gardes", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isPartner", ignore = true)
    void updateEntityFromRequest(
            PharmacieRequest request,
            @MappingTarget Pharmacie pharmacie
    );
}