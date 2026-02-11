package com.datecourse.service;

import com.datecourse.domain.StationGenerator;
import com.datecourse.domain.station.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DateCourseService {

    private final StationGenerator generator;

    public Station getRandomStationInLine2() {
        return generator.generate();
    }
}
