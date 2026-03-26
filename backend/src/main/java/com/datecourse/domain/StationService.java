package com.datecourse.domain;

import com.datecourse.domain.station.Station;
import com.datecourse.domain.station.StationGenerator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StationService {

    private final SubwayStationReader subwayStationReader;
    private final StationGenerator stationGenerator;

    public Station getRandomStation(String city, String district) {
        List<Station> stations = subwayStationReader.readStationBy(city, district);
        return stationGenerator.generate(stations);
    }
}
