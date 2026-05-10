package com.pharmago.backend.dto.response;

import java.util.List;

public record ImportResultResponse(
        int total,
        int importees,
        int doublons,
        int erreurs,
        List<String> pharmaciesImportees,
        List<String> erreurDetails
) {}