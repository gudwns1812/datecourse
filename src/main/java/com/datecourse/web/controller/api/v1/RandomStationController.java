package com.datecourse.web.controller.api.v1;

import com.datecourse.domain.station.Station;
import com.datecourse.service.DateCourseService;
import com.datecourse.web.controller.dto.StationResponseDto;
import com.datecourse.web.support.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/stations/random")
public class RandomStationController {

    private final DateCourseService dateCourseService;

    @GetMapping
    public ApiResponse<StationResponseDto> getRandomSubway() {
        Station station = dateCourseService.getRandomStation();

        return ApiResponse.success(StationResponseDto.of(station));
    }
}
