package com.datecourse.domain.station;

import lombok.Getter;
import org.locationtech.jts.geom.Point;

@Getter
public class Station {
    private final String line;
    private final String stationName;

    private final Double latitude;
    private final Double longitude;

    private Station(String line, String stationName, Double latitude, Double longitude) {
        this.line = line;
        this.stationName = stationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Station of(String line, String stationName, Point location) {
        double longitude = location.getCoordinate().getX();
        double latitude = location.getCoordinate().getY();
        return new Station(line, stationName, latitude, longitude);
    }
}
