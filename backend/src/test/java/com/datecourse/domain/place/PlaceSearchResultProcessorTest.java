package com.datecourse.domain.place;

import static org.assertj.core.api.Assertions.assertThat;

import com.datecourse.storage.entity.*;
import com.datecourse.support.utils.GeometryUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PlaceSearchResultProcessorTest {

    private PlaceSearchResultProcessor placeSearchResultProcessor;

    private Place cafe;
    private Place bakery;

    @BeforeEach
    void setUp() {
        placeSearchResultProcessor = new PlaceSearchResultProcessor(
                new PlaceSearchResultFactory(
                        new PlaceDistanceProcessor(),
                        new PlaceRecommendationScoreProcessor()
                )
        );

        cafe = Place.create(
                "City Cafe",
                "Cafe",
                "Cozy coffee spot",
                FullAddress.of("Seoul", "Jung","detail"),
                GeometryUtils.createPoint(126.9780, 37.5665)
        );
        bakery = Place.create(
                "Morning Bakery",
                "Food",
                "Fresh brunch and coffee",
                FullAddress.of("Seoul", "Jongno","detail"),
                GeometryUtils.createPoint(126.9781, 37.5666)
        );

        ReflectionTestUtils.setField(cafe, "id", 1L);
        ReflectionTestUtils.setField(bakery, "id", 2L);

        Tag coffee = Tag.create("coffee");
        Tag brunch = Tag.create("brunch");
        ReflectionTestUtils.setField(coffee, "id", 10L);
        ReflectionTestUtils.setField(brunch, "id", 11L);

        PlaceTag cafeTag = PlaceTag.create(cafe, coffee);
        PlaceTag bakeryTag = PlaceTag.create(bakery, brunch);
        ReflectionTestUtils.setField(cafe, "placeTags", Set.of(cafeTag));
        ReflectionTestUtils.setField(bakery, "placeTags", Set.of(bakeryTag));
    }

    @Test
    void process_ordersByRecommendationScoreAndDistance() {
        List<PlaceSearchResult> result = placeSearchResultProcessor.process(
                List.of(bakery, cafe),
                new PlaceSearchCommand(37.5665, 126.9780, "coffee", null, 2000d, 20)
        );

        assertThat(result)
                .extracting(PlaceSearchResult::name)
                .containsExactly("City Cafe", "Morning Bakery");
    }

    @Test
    void process_filtersPlacesOutsideRadius() {
        Place farAway = Place.create(
                "Far Place",
                "Travel",
                "Scenic spot",
                FullAddress.of("Seoul", "Gangseo","detail"),
                GeometryUtils.createPoint(126.8000, 37.5600)
        );
        ReflectionTestUtils.setField(farAway, "id", 3L);

        List<PlaceSearchResult> result = placeSearchResultProcessor.process(
                List.of(farAway),
                new PlaceSearchCommand(37.5665, 126.9780, null, null, 500d, 20)
        );

        assertThat(result).isEmpty();
    }

    @Test
    void process_limitsResultsToMaximumTwoHundred() {
        List<Place> manyPlaces = new ArrayList<>();

        for (long index = 1; index <= 250; index++) {
            Place place = Place.create(
                    "Place " + index,
                    "Cafe",
                    "Desc",
                    FullAddress.of("Seoul", "Jung", "detail"),
                    GeometryUtils.createPoint(126.9780 + (index * 0.000001d), 37.5665 + (index * 0.000001d))
            );
            ReflectionTestUtils.setField(place, "id", index);
            manyPlaces.add(place);
        }

        List<PlaceSearchResult> result = placeSearchResultProcessor.process(
                manyPlaces,
                new PlaceSearchCommand(37.5665, 126.9780, null, null, 5000d, 500)
        );

        assertThat(result).hasSize(PlaceSearchCommand.MAX_SIZE);
    }
}
