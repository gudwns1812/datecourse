package com.datecourse.service;

import com.datecourse.domain.station.Station;
import com.datecourse.domain.station.StationGenerator;
import com.datecourse.storage.repository.StationMemoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DateCourseService {

    private final StationMemoryRepository stationRepository;
    private final StationGenerator stationGenerator;

    public Station getRandomStation() {
        List<Station> stations = stationRepository.findAll();
        return stationGenerator.generate(stations);
    }
}
