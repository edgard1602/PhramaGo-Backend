package com.pharmago.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GooglePlacesResponse(
        List<GooglePlaceResult> places,
        @JsonProperty("nextPageToken")
        String nextPageToken
) {
    public record GooglePlaceResult(
            String id,
            DisplayName displayName,
            String formattedAddress,
            @JsonProperty("nationalPhoneNumber")
            String nationalPhoneNumber,
            @JsonProperty("internationalPhoneNumber")
            String internationalPhoneNumber,
            Location location
    ) {}

    public record DisplayName(
            String text,
            String languageCode
    ) {}

    public record Location(
            Double latitude,
            Double longitude
    ) {}
}