package com.datecourse.storage.repository;

import com.datecourse.storage.entity.Place;
import java.util.List;

public interface PlaceRepositoryCustom {

    List<Place> findCandidates(double latitude, double longitude, double radiusMeters);
}
