package com.datecourse.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.datecourse.domain.place.PlaceSearchBounds;
import com.datecourse.storage.entity.FullAddress;
import com.datecourse.storage.entity.Place;
import com.datecourse.storage.entity.PlaceTag;
import com.datecourse.storage.entity.Tag;
import com.datecourse.storage.repository.PlaceRepository;
import com.datecourse.storage.repository.PlaceTagTestRepository;
import com.datecourse.storage.repository.TagTestRepository;
import com.datecourse.support.utils.GeometryUtils;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

class PlaceRepositoryQueryTest extends ServiceConnectionIntegrationTest {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private TagTestRepository tagRepository;

    @Autowired
    private PlaceTagTestRepository placeTagRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("TRUNCATE TABLE place, place_tag, tag RESTART IDENTITY CASCADE;");
    }

    @DisplayName("반경 안에 있는 장소와 태그를 함께 조회한다")
    @Test
    void findCandidates_returnsPlacesWithinRadius() {
        Place cafe = savePlace("Blue Bottle", "Cafe", "Specialty coffee", "Jung-gu", 126.9780, 37.5665);
        Place nearbyFood = savePlace("Brunch Place", "Food", "Popular brunch", "Jongno-gu", 126.9781, 37.5666);
        Place farAway = savePlace("Far Travel", "Travel", "Far away spot", "Gangseo-gu", 126.8000, 37.5600);

        attachTag(cafe, "coffee");
        attachTag(nearbyFood, "brunch");
        attachTag(farAway, "travel");

        List<Place> result = placeRepository.findCandidates(37.5665, 126.9780, 500d);

        result.forEach((r) -> System.out.println(r.getName()) );
        assertThat(result)
                .extracting(Place::getName)
                .containsExactlyInAnyOrder("Blue Bottle", "Brunch Place");
        assertThat(result)
                .filteredOn(place -> place.getName().equals("Blue Bottle"))
                .singleElement()
                .satisfies(place -> assertThat(place.getTagNames()).containsExactly("coffee"));
    }

    @DisplayName("반경 밖 장소는 조회 대상에서 제외한다")
    @Test
    void findCandidates_excludesPlacesOutsideRadius() {
        Place nearby = savePlace("Near Cafe", "Cafe", "Near place", "Jung-gu", 126.9780, 37.5665);
        Place border = savePlace("Far Cafe", "Cafe", "Outside radius", "Jongno-gu", 126.9781, 37.5666);

        attachTag(nearby, "coffee");
        attachTag(border, "coffee");

        List<Place> result = placeRepository.findCandidates(37.5665, 126.9780, 10d);

        assertThat(result)
                .extracting(Place::getName)
                .containsExactly("Near Cafe");
    }

    @DisplayName("soft delete 된 장소는 반경 안에 있어도 제외한다")
    @Test
    void findCandidates_excludesSoftDeletedPlace() {
        Place deletedPlace = savePlace("Deleted Brunch", "Food", "Deleted place", "Jongno-gu", 126.97805, 37.56655);
        Place alivePlace = savePlace("Alive Brunch", "Food", "Active place", "Jongno-gu", 126.97808, 37.56658);

        attachTag(deletedPlace, "brunch");
        attachTag(alivePlace, "brunch");
        placeRepository.delete(deletedPlace);

        List<Place> result = placeRepository.findCandidates(37.5665, 126.9780, 500d);

        assertThat(result)
                .extracting(Place::getName)
                .containsExactly("Alive Brunch");
    }

    @Test
    void findCandidatesWithinBounds_returnsPlacesInsideBounds() {
        Place insideCafe = savePlace("Inside Cafe", "Cafe", "Inside bounds", "Jung-gu", 126.9780, 37.5665);
        Place insideFood = savePlace("Inside Food", "Food", "Inside bounds", "Jongno-gu", 126.9783, 37.5667);
        Place outsidePlace = savePlace("Outside Place", "Travel", "Outside bounds", "Gangseo-gu", 126.9900, 37.5800);

        attachTag(insideCafe, "coffee");
        attachTag(insideFood, "brunch");
        attachTag(outsidePlace, "travel");

        List<Place> result = placeRepository.findCandidatesWithinBounds(
                new PlaceSearchBounds(37.5660, 126.9775, 37.5670, 126.9785)
        );

        assertThat(result)
                .extracting(Place::getName)
                .containsExactlyInAnyOrder("Inside Cafe", "Inside Food");
        assertThat(result)
                .filteredOn(place -> place.getName().equals("Inside Cafe"))
                .singleElement()
                .satisfies(place -> assertThat(place.getTagNames()).containsExactly("coffee"));
    }

    @Test
    void findCandidatesWithinBounds_excludesSoftDeletedPlace() {
        Place deletedPlace = savePlace("Deleted Cafe", "Cafe", "Deleted", "Jung-gu", 126.9780, 37.5665);
        Place alivePlace = savePlace("Alive Cafe", "Cafe", "Alive", "Jung-gu", 126.9782, 37.5667);

        attachTag(deletedPlace, "coffee");
        attachTag(alivePlace, "coffee");
        placeRepository.delete(deletedPlace);

        List<Place> result = placeRepository.findCandidatesWithinBounds(
                new PlaceSearchBounds(37.5660, 126.9775, 37.5670, 126.9785)
        );

        assertThat(result)
                .extracting(Place::getName)
                .containsExactly("Alive Cafe");
    }

    private Place savePlace(
            String name,
            String category,
            String description,
            String district,
            double longitude,
            double latitude
    ) {
        return placeRepository.saveAndFlush(Place.create(
                name,
                category,
                description,
                FullAddress.of("Seoul", district, "detail"),
                GeometryUtils.createPoint(longitude, latitude)
        ));
    }

    private void attachTag(Place place, String tagName) {
        Tag tag = tagRepository.saveAndFlush(Tag.create(tagName));
        placeTagRepository.saveAndFlush(PlaceTag.create(place, tag));
    }
}
