package com.datecourse.storage.repository;

import com.datecourse.storage.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagTestRepository extends JpaRepository<Tag, Long> {
}
