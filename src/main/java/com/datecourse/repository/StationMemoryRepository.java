package com.datecourse.repository;

import com.datecourse.domain.station.Station;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class StationMemoryRepository {
    private static final Map<Long, Station> store = new ConcurrentHashMap<>();
    private static Long sequence = 0L;

    public List<Station> findAll() {
        return List.copyOf(store.values());
    }

    public Station save(Station station) {
        store.put(++sequence, station);
        return station;
    }
}
