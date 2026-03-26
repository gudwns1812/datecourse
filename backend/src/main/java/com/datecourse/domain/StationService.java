package com.datecourse.domain;

import com.datecourse.domain.station.Station;
import com.datecourse.domain.station.StationGenerator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DateCourseService {

    private final SubwayStationReader subwayStationReader;
    private final StationGenerator stationGenerator;

    public Station getRandomStation() {
        List<Station> stations = subwayStationReader.readAll();
        return stationGenerator.generate(stations);
    }
}
