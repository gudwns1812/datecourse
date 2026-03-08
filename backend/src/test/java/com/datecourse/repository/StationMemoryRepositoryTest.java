package com.datecourse.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.datecourse.domain.station.Station;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StationMemoryRepositoryTest {

    @Autowired
    StationMemoryRepository repository;

    @Test
    void findAll() {
        //given
        //when
        List<Station> stations = repository.findAll();
        //then
        assertThat(stations).isNotEmpty();
    }
}
