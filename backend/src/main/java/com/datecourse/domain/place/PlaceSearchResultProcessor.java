package com.datecourse.domain.place;

import com.datecourse.storage.entity.Place;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceSearchResultProcessor {

    private final PlaceSearchResultFactory placeSearchResultFactory;

    public List<PlaceSearchResult> process(List<Place> places, PlaceSearchCommand command) {
        return places.stream()
                .map(place -> placeSearchResultFactory.create(place, command))
                .filter(Objects::nonNull)
                .filter(result -> result.distanceMeters() <= command.radiusMeters())
                .sorted(byRecommendation())
                .limit(command.size())
                .toList();
    }

    private Comparator<PlaceSearchResult> byRecommendation() {
        return Comparator.comparingDouble(PlaceSearchResult::recommendationScore)
                .reversed()
                .thenComparingDouble(PlaceSearchResult::distanceMeters)
                .thenComparing(PlaceSearchResult::name, String.CASE_INSENSITIVE_ORDER);
    }
}
