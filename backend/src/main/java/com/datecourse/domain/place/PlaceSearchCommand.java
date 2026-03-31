package com.datecourse.domain.place;

public record PlaceSearchCommand(
        double latitude,
        double longitude,
        String query,
        String category,
        double radiusMeters,
        int size,
        PlaceSearchBounds bounds
) {

    public static final int MAX_SIZE = 200;

    public PlaceSearchCommand(double latitude, double longitude, String query, String category, double radiusMeters, int size) {
        this(latitude, longitude, query, category, radiusMeters, size, null);
    }

    public boolean usesBounds() {
        return bounds != null && bounds.isValid();
    }

    public int limitedSize() {
        if (size <= 0) {
            return MAX_SIZE;
        }

        return Math.min(size, MAX_SIZE);
    }
}
