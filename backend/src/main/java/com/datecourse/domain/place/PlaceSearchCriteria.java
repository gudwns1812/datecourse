package com.datecourse.domain.place;

public record PlaceSearchCriteria(
        double latitude,
        double longitude,
        String query,
        String category,
        double radiusMeters,
        int size
) {
}
