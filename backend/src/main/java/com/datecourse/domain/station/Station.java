package com.datecourse.domain.station;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Station {
    private List<String> lines;
    private String stationName;
    private Address address;

    private Station(String line, String stationName, String simpleAddress) {
        this.lines = List.of(line);
        this.stationName = stationName;
        this.address = Address.from(simpleAddress);
    }

    /**
     * Station 생성을 위한 정적 팩토리 메서드
     */
    public static Station of(String line, String stationName, String simpleAddress) {
        return new Station(line, stationName, simpleAddress);
    }
}
