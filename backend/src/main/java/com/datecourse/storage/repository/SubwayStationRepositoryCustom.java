package com.datecourse.storage.repository;

import com.datecourse.storage.entity.SubwayStation;
import java.util.List;

public interface SubwayStationRepositoryCustom {
    List<SubwayStation> findByFilter(String city, String district);
}
