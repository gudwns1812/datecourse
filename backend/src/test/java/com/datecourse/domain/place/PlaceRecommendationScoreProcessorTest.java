package com.datecourse.domain.place;

import static org.assertj.core.api.Assertions.assertThat;

import com.datecourse.storage.entity.FullAddress;
import com.datecourse.storage.entity.Place;
import com.datecourse.storage.entity.PlaceTag;
import com.datecourse.storage.entity.Tag;
import com.datecourse.support.utils.GeometryUtils;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PlaceRecommendationScoreProcessorTest {

    private PlaceRecommendationScoreProcessor placeRecommendationScoreProcessor;

    @BeforeEach
    void setUp() {
        placeRecommendationScoreProcessor = new PlaceRecommendationScoreProcessor();
    }

    @Test
    void calculate_addsScoreWhenQueryMatchesName() {
        Place place = createPlace("Coffee House", "Cafe", "Quiet place", "dessert");

        double score = placeRecommendationScoreProcessor.calculate(place, "coffee", 0d, 0d);

        assertThat(score).isEqualTo(100d);
    }

    @Test
    void calculate_addsScoreWhenQueryMatchesCategory() {
        Place place = createPlace("Morning House", "Brunch Cafe", "Quiet place", "dessert");

        double score = placeRecommendationScoreProcessor.calculate(place, "cafe", 0d, 0d);

        assertThat(score).isEqualTo(60d);
    }

    @Test
    void calculate_ignoresDescriptionAndTagsForTextScore() {
        Place place = createPlace("Morning House", "Food", "Coffee is only in description", "coffee");

        double score = placeRecommendationScoreProcessor.calculate(place, "coffee", 0d, 0d);

        assertThat(score).isEqualTo(0d);
    }

    private Place createPlace(String name, String category, String description, String tagName) {
        Place place = Place.create(
                name,
                category,
                description,
                FullAddress.of("Seoul", "Jung-gu", "detail"),
                GeometryUtils.createPoint(126.9780, 37.5665)
        );
        ReflectionTestUtils.setField(place, "id", 1L);

        Tag tag = Tag.create(tagName);
        ReflectionTestUtils.setField(tag, "id", 2L);
        PlaceTag placeTag = PlaceTag.create(place, tag);
        ReflectionTestUtils.setField(place, "placeTags", Set.of(placeTag));
        return place;
    }
}
