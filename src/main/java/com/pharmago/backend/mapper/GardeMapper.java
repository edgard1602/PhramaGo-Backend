package com.pharmago.backend.mapper;

import com.pharmago.backend.dto.response.GardeResponse;
import com.pharmago.backend.entity.Garde;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface GardeMapper {

    @Mapping(target = "pharmacieId",
            source = "pharmacie.id")
    @Mapping(target = "pharmacieNom",
            source = "pharmacie.nom")
    GardeResponse toResponse(Garde garde);
}