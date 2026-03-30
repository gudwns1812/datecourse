package com.datecourse.domain.place;

import org.springframework.stereotype.Component;

@Component
public class PlaceDistanceProcessor {

    public double calculate(double latitude1, double longitude1, double latitude2, double longitude2) {
        double earthRadiusMeters = 6_371_000d;
        double deltaLatitude = Math.toRadians(latitude2 - latitude1);
        double deltaLongitude = Math.toRadians(longitude2 - longitude1);

        double a = Math.sin(deltaLatitude / 2d) * Math.sin(deltaLatitude / 2d)
                + Math.cos(Math.toRadians(latitude1))
                * Math.cos(Math.toRadians(latitude2))
                * Math.sin(deltaLongitude / 2d)
                * Math.sin(deltaLongitude / 2d);

        double c = 2d * Math.atan2(Math.sqrt(a), Math.sqrt(1d - a));
        return earthRadiusMeters * c;
    }
}
