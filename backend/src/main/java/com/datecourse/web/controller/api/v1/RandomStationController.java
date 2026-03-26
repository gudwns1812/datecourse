package com.datecourse.web.controller.api.v1;

import com.datecourse.domain.StationService;
import com.datecourse.domain.station.Station;
import com.datecourse.support.auth.MemberDetails;
import com.datecourse.support.response.ApiResponse;
import com.datecourse.web.controller.dto.StationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/stations")
public class RandomStationController {

    private final StationService stationService;

    @GetMapping("/random")
    public ApiResponse<StationResponseDto> getRandomSubway(
            @AuthenticationPrincipal MemberDetails member,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district
    ) {
        Station station = stationService.getRandomStation(city, district);

        return ApiResponse.success(StationResponseDto.of(station));
    }
}
