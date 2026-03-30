package com.datecourse.domain.place;

import com.datecourse.storage.entity.Place;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceSearchCommandProcessor placeSearchCommandProcessor;
    private final PlaceSearchReader placeSearchReader;
    private final PlaceSearchResultProcessor placeSearchResultProcessor;

    public List<PlaceSearchResult> getPlaces(PlaceSearchCommand command) {
        PlaceSearchCriteria criteria = placeSearchCommandProcessor.process(command);

        List<Place> candidates = placeSearchReader.read(criteria);

        return placeSearchResultProcessor.process(candidates, criteria);
    }
}
