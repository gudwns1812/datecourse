package com.datecourse.storage.repository;

import com.datecourse.storage.entity.SubwayStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationMemoryRepository extends JpaRepository<SubwayStation, Integer> {
}
