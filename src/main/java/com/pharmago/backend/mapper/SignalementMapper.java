package com.pharmago.backend.mapper;

import com.pharmago.backend.dto.response.SignalementResponse;
import com.pharmago.backend.entity.Signalement;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SignalementMapper {

    @Mapping(target = "pharmacieId",
            source = "pharmacie.id")
    @Mapping(target = "pharmacieNom",
            source = "pharmacie.nom")
    SignalementResponse toResponse(Signalement signalement);
}