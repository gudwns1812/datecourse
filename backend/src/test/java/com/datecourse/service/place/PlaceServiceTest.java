package com.datecourse.service.place;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.datecourse.domain.place.PlaceSearchCommand;
import com.datecourse.domain.place.PlaceSearchBounds;
import com.datecourse.domain.place.PlaceSearchFilterProcessor;
import com.datecourse.domain.place.PlaceSearchReader;
import com.datecourse.domain.place.PlaceSearchResult;
import com.datecourse.domain.place.PlaceSearchResultProcessor;
import com.datecourse.domain.place.PlaceService;
import com.datecourse.storage.entity.Place;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {

    @Mock
    private PlaceSearchReader placeSearchReader;

    @Mock
    private PlaceSearchFilterProcessor placeSearchFilterProcessor;

    @Mock
    private PlaceSearchResultProcessor placeSearchResultProcessor;

    @InjectMocks
    private PlaceService placeService;

    @DisplayName("서비스는 반경 조회 후 이름/카테고리 필터링과 결과 가공을 순서대로 조합한다")
    @Test
    void getPlaces_orchestratesSearchFlow() {
        PlaceSearchCommand command = new PlaceSearchCommand(37.5665, 126.9780, "coffee", "Cafe", 0d, 0);
        Place candidate = org.mockito.Mockito.mock(Place.class);
        Place filteredPlace = org.mockito.Mockito.mock(Place.class);
        List<Place> candidates = List.of(candidate);
        List<Place> filteredPlaces = List.of(filteredPlace);
        List<PlaceSearchResult> expected = List.of(
                new PlaceSearchResult(
                        1L,
                        "City Cafe",
                        "Cafe",
                        "Cozy coffee spot",
                        37.5665,
                        126.9780,
                        12.3,
                        178.9,
                        List.of("coffee")
                )
        );

        given(placeSearchReader.read(command)).willReturn(candidates);
        given(placeSearchFilterProcessor.process(candidates, command)).willReturn(filteredPlaces);
        given(placeSearchResultProcessor.process(filteredPlaces, command)).willReturn(expected);

        List<PlaceSearchResult> result = placeService.getPlaces(command);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getPlaces_orchestratesBoundsSearchFlow() {
        PlaceSearchCommand command = new PlaceSearchCommand(
                37.5665,
                126.9780,
                "coffee",
                "Cafe",
                150d,
                200,
                new PlaceSearchBounds(37.5660, 126.9775, 37.5670, 126.9785)
        );
        List<Place> candidates = List.of(org.mockito.Mockito.mock(Place.class));
        List<Place> filteredPlaces = List.of(org.mockito.Mockito.mock(Place.class));
        List<PlaceSearchResult> expected = List.of();

        given(placeSearchReader.read(command)).willReturn(candidates);
        given(placeSearchFilterProcessor.process(candidates, command)).willReturn(filteredPlaces);
        given(placeSearchResultProcessor.process(filteredPlaces, command)).willReturn(expected);

        List<PlaceSearchResult> result = placeService.getPlaces(command);

        assertThat(result).isEmpty();
    }
}
