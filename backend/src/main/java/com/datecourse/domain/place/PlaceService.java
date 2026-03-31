package com.datecourse.domain.place;

import com.datecourse.storage.entity.Place;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceSearchReader placeSearchReader;
    private final PlaceSearchFilterProcessor placeSearchFilterProcessor;
    private final PlaceSearchResultProcessor placeSearchResultProcessor;

    public List<PlaceSearchResult> getPlaces(PlaceSearchCommand command) {
        List<Place> candidates = placeSearchReader.read(command);
        List<Place> filteredPlaces = placeSearchFilterProcessor.process(candidates, command);

        return placeSearchResultProcessor.process(filteredPlaces, command);
    }
}
