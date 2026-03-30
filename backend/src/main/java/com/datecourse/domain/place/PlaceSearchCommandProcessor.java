package com.datecourse.domain.place;

import org.springframework.stereotype.Component;

@Component
public class PlaceSearchCommandProcessor {

    private static final double DEFAULT_RADIUS_METERS = 2_000d;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 50;

    public PlaceSearchCriteria process(PlaceSearchCommand command) {
        return new PlaceSearchCriteria(
                command.latitude(),
                command.longitude(),
                normalizeQuery(command.query()),
                command.category(),
                normalizeRadius(command.radiusMeters()),
                normalizeSize(command.size())
        );
    }

    private String normalizeQuery(String query) {
        if (query == null) {
            return null;
        }

        String trimmed = query.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private double normalizeRadius(double radiusMeters) {
        if (radiusMeters <= 0d) {
            return DEFAULT_RADIUS_METERS;
        }

        return radiusMeters;
    }

    private int normalizeSize(int size) {
        if (size <= 0) {
            return DEFAULT_SIZE;
        }

        return Math.min(size, MAX_SIZE);
    }
}
