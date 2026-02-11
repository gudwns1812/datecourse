package com.datecourse.web.controller.dto;

import com.datecourse.domain.station.Station;

import java.util.List;

public record StationResponseDto(
        List<String> lineNumbers,
        String stationName,
        String stationAddress
) {
    public static StationResponseDto of(Station station) {
        return new StationResponseDto(
                station.getLines(),
                station.getStationName(),
                station.getAddress().getAddressWithoutDetail()
        );
    }
}
