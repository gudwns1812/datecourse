package com.datecourse.domain.place;

import com.datecourse.storage.entity.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceSearchResultFactory {

    private final PlaceDistanceProcessor placeDistanceProcessor;
    private final PlaceRecommendationScoreProcessor placeRecommendationScoreProcessor;

    public PlaceSearchResult create(Place place, PlaceSearchCriteria criteria) {
        Double latitude = place.getLatitude();
        Double longitude = place.getLongitude();
        if (latitude == null || longitude == null) {
            return null;
        }

        double distanceMeters = placeDistanceProcessor.calculate(
                criteria.latitude(),
                criteria.longitude(),
                latitude,
                longitude
        );
        double recommendationScore = placeRecommendationScoreProcessor.calculate(
                place,
                criteria.query(),
                distanceMeters,
                criteria.radiusMeters()
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
