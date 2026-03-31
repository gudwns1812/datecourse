package com.datecourse.domain.place;

import static org.assertj.core.api.Assertions.assertThat;

import com.datecourse.storage.entity.FullAddress;
import com.datecourse.storage.entity.Place;
import com.datecourse.storage.entity.PlaceTag;
import com.datecourse.storage.entity.Tag;
import com.datecourse.support.utils.GeometryUtils;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PlaceSearchFilterProcessorTest {

    private PlaceSearchFilterProcessor placeSearchFilterProcessor;

    private Place cityCafe;
    private Place brunchPlace;
    private Place tagOnlyMatchPlace;
    private Place descriptionOnlyMatchPlace;

    @BeforeEach
    void setUp() {
        placeSearchFilterProcessor = new PlaceSearchFilterProcessor();

        cityCafe = createPlace(1L, "City Cafe", "Cafe", "Cozy coffee spot", 126.9780, 37.5665);
        brunchPlace = createPlace(2L, "Morning Brunch", "Food", "Fresh brunch", 126.9781, 37.5666);
        tagOnlyMatchPlace = createPlace(3L, "Silent Place", "Food", "No query in name or category", 126.9782, 37.5667);
        descriptionOnlyMatchPlace = createPlace(4L, "Hidden Spot", "Travel", "Coffee is only in description", 126.9783, 37.5668);

        attachTag(cityCafe, "dessert");
        attachTag(brunchPlace, "brunch");
        attachTag(tagOnlyMatchPlace, "coffee");
        attachTag(descriptionOnlyMatchPlace, "travel");
    }

    @Test
    void process_filtersByExactCategory() {
        List<Place> result = placeSearchFilterProcessor.process(
                List.of(cityCafe, brunchPlace),
                new PlaceSearchCommand(37.5665, 126.9780, null, "Cafe", 2000d, 20)
        );

        assertThat(result)
                .extracting(Place::getName)
                .containsExactly("City Cafe");
    }

    @Test
    void process_filtersByQueryAgainstNameAndCategoryOnly() {
        List<Place> result = placeSearchFilterProcessor.process(
                List.of(cityCafe, brunchPlace, tagOnlyMatchPlace, descriptionOnlyMatchPlace),
                new PlaceSearchCommand(37.5665, 126.9780, "coffee", null, 2000d, 20)
        );

        assertThat(result)
                .extracting(Place::getName)
                .isEmpty();
    }

    @Test
    void process_keepsPlacesWhenQueryMatchesNameOrCategory() {
        List<Place> result = placeSearchFilterProcessor.process(
                List.of(cityCafe, brunchPlace),
                new PlaceSearchCommand(37.5665, 126.9780, "cafe", null, 2000d, 20)
        );

        assertThat(result)
                .extracting(Place::getName)
                .containsExactly("City Cafe");
    }

    private Place createPlace(
            Long id,
            String name,
            String category,
            String description,
            double longitude,
            double latitude
    ) {
        Place place = Place.create(
                name,
                category,
                description,
                FullAddress.of("Seoul", "Jung-gu", "detail"),
                GeometryUtils.createPoint(longitude, latitude)
        );
        ReflectionTestUtils.setField(place, "id", id);
        return place;
    }

    private void attachTag(Place place, String tagName) {
        Tag tag = Tag.create(tagName);
        ReflectionTestUtils.setField(tag, "id", place.getId() + 100L);
        PlaceTag placeTag = PlaceTag.create(place, tag);
        ReflectionTestUtils.setField(place, "placeTags", Set.of(placeTag));
    }
}
