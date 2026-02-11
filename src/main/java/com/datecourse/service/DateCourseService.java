package com.datecourse.service;

import com.datecourse.domain.StationGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DateCourseService {

    private final StationGenerator generator;

    public String getRandomStationInLine2() {
        return generator.generate();
    }
}
