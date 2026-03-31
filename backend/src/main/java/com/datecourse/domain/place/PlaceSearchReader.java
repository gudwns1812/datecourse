package com.datecourse.domain.place;

import com.datecourse.storage.entity.Place;
import com.datecourse.storage.repository.PlaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceSearchReader {

    private final PlaceRepository placeRepository;

    public List<Place> read(PlaceSearchCommand command) {
        return placeRepository.findCandidates(
                command.latitude(),
                command.longitude(),
                command.radiusMeters()
        );
    }
}
