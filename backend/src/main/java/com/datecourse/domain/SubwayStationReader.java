package com.datecourse.domain;

import com.datecourse.domain.station.Station;
import com.datecourse.storage.entity.SubwayStation;
import com.datecourse.storage.repository.SubwayStationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubwayStationReader {

    private final SubwayStationRepository subwayStationRepository;

    public List<Station> readAll() {
        List<SubwayStation> stations = subwayStationRepository.findAll();

        return stations.stream()
                .map(SubwayStation::toDomain)
                .toList();
    }
}
