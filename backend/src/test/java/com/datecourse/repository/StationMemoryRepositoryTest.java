package com.datecourse.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.datecourse.domain.station.Station;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class StationMemoryRepositoryTest {

    @Autowired
    StationMemoryRepository repository;

    @MockitoBean
    MemberRepository memberRepository;

    @Test
    void findAll() {
        //given
        //when
        List<Station> stations = repository.findAll();
        //then
        assertThat(stations).isNotEmpty();
    }
}
