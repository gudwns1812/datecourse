package com.datecourse.storage.repository;

import com.datecourse.storage.entity.PlaceTag;
import com.datecourse.storage.entity.PlaceTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceTagTestRepository extends JpaRepository<PlaceTag, PlaceTagId> {
}
