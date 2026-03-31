package com.datecourse.domain.place;

import org.springframework.stereotype.Component;

@Component
public class PlaceSearchCommandFactory {

    public PlaceSearchCommand create(
            double latitude,
            double longitude,
            String query,
            String category,
            double radiusMeters,
            int size,
            PlaceSearchBounds bounds
    ) {
        if (bounds != null && bounds.isValid()) {
            return new PlaceSearchCommand(
                    bounds.centerLatitude(),
                    bounds.centerLongitude(),
                    query,
                    category,
                    bounds.maximumDistanceMeters(),
                    size,
                    bounds
            );
        }

        return new PlaceSearchCommand(latitude, longitude, query, category, radiusMeters, size, null);
    }
}
