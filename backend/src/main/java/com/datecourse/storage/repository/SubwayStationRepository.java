package com.datecourse.storage.repository;

import com.datecourse.storage.entity.SubwayStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubwayStationRepository extends JpaRepository<SubwayStation, Integer>, SubwayStationRepositoryCustom {
}
