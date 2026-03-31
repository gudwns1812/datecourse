package com.datecourse.domain.place;

import com.datecourse.storage.entity.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceSearchResultFactory {

    private final PlaceDistanceProcessor placeDistanceProcessor;
    private final PlaceRecommendationScoreProcessor placeRecommendationScoreProcessor;

    public PlaceSearchResult create(Place place, PlaceSearchCommand command) {
        Double latitude = place.getLatitude();
        Double longitude = place.getLongitude();
        if (latitude == null || longitude == null) {
            return null;
        }

        double distanceMeters = placeDistanceProcessor.calculate(
                command.latitude(),
                command.longitude(),
                latitude,
                longitude
        );
        double recommendationScore = placeRecommendationScoreProcessor.calculate(
                place,
                command.query(),
                distanceMeters,
                command.radiusMeters()
        );

        return new PlaceSearchResult(
                place.getId(),
                place.getName(),
                place.getCategory(),
                place.getDescription(),
                latitude,
                longitude,
                distanceMeters,
                recommendationScore,
                place.getTagNames()
        );
    }
}
