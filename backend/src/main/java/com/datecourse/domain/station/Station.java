package com.datecourse.domain.station;

import lombok.Getter;

import java.util.List;

@Getter
public class Station {
    private List<String> lines;
    private String stationName;
    private Address address;

    public Station(String line, String stationName, String simpleAddress) {
        this.lines = List.of(line);
        this.stationName = stationName;
        this.address = Address.from(simpleAddress);
    }
}
