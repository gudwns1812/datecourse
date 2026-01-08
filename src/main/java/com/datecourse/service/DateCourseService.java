package com.datecourse.service;

import com.datecourse.domain.PlaceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DateCourseService {

    private final PlaceGenerator generator;

    public String getRandomPlaceInLine2() {
        return generator.generate();
    }
}
