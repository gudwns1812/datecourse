package com.datecourse.web.controller.api.v1;

import com.datecourse.service.DateCourseService;
import com.datecourse.web.support.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/random/subway")
public class RandomSubwayController {

    private final DateCourseService dateCourseService;

    @GetMapping
    public ApiResponse<String> getRandomSubway() {
        String randomSubwayLine2 = dateCourseService.getRandomPlaceInLine2();

        return ApiResponse.success(randomSubwayLine2);
    }
}
