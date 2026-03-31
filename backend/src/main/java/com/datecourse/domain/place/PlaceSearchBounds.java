package com.datecourse.domain.place;

public record PlaceSearchBounds(
        double southWestLatitude,
        double southWestLongitude,
        double northEastLatitude,
        double northEastLongitude
) {

    private static final double EARTH_RADIUS_METERS = 6_371_000d;

    public boolean isValid() {
        return Double.isFinite(southWestLatitude)
                && Double.isFinite(southWestLongitude)
                && Double.isFinite(northEastLatitude)
                && Double.isFinite(northEastLongitude)
                && southWestLatitude <= northEastLatitude
                && southWestLongitude <= northEastLongitude;
    }

    public double centerLatitude() {
        return (southWestLatitude + northEastLatitude) / 2d;
    }

    public double centerLongitude() {
        return (southWestLongitude + northEastLongitude) / 2d;
    }

    public double maximumDistanceMeters() {
        return distanceMeters(centerLatitude(), centerLongitude(), northEastLatitude, northEastLongitude);
    }

    private double distanceMeters(double latitude1, double longitude1, double latitude2, double longitude2) {
        double deltaLatitude = Math.toRadians(latitude2 - latitude1);
        double deltaLongitude = Math.toRadians(longitude2 - longitude1);

        double a = Math.sin(deltaLatitude / 2d) * Math.sin(deltaLatitude / 2d)
                + Math.cos(Math.toRadians(latitude1))
                * Math.cos(Math.toRadians(latitude2))
                * Math.sin(deltaLongitude / 2d)
                * Math.sin(deltaLongitude / 2d);

        double c = 2d * Math.atan2(Math.sqrt(a), Math.sqrt(1d - a));
        return EARTH_RADIUS_METERS * c;
    }
}
