package com.datecourse.web.controller.dto;

import com.datecourse.domain.station.Station;

public record StationResponseDto(
        String line,
        String stationName,
        Double longitude,
        Double latitude
) {
    public static StationResponseDto of(Station station) {
        return new StationResponseDto(
                station.getLine(),
                station.getStationName(),
                station.getLongitude(),
                station.getLatitude()
        );
    }
}
