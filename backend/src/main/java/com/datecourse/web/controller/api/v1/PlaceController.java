package com.datecourse.web.controller.api.v1;

import com.datecourse.domain.place.PlaceSearchCommand;
import com.datecourse.domain.place.PlaceSearchResult;
import com.datecourse.domain.place.PlaceService;
import com.datecourse.support.auth.MemberDetails;
import com.datecourse.support.response.ApiResponse;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/places")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public ApiResponse<List<PlaceSearchResult>> getPlaces(
            @AuthenticationPrincipal MemberDetails member,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "2000") double radius,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<PlaceSearchResult> places = placeService.getPlaces(new PlaceSearchCommand(latitude, longitude, query, category, radius, size));

        return ApiResponse.success(places);
    }
}
