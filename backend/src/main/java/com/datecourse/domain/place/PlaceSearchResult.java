package com.datecourse.domain.place;

import java.util.List;

public record PlaceSearchResult(
        Long id,
        String name,
        String category,
        String description,
        Double latitude,
        Double longitude,
        double distanceMeters,
        double recommendationScore,
        List<String> tags
) {
}
