package com.datecourse.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.datecourse.storage.entity.BaseAddress;
import com.datecourse.storage.entity.SubwayStation;
import com.datecourse.storage.repository.SubwayStationRepository;
import jakarta.persistence.EntityManager;
import java.lang.reflect.Constructor;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class SubwayStationRepositorySoftDeleteTest {

    @Autowired
    SubwayStationRepository repository;

    @Autowired
    EntityManager entityManager;

    @DisplayName("soft delete 시 row는 남아있고 일반 조회에서는 제외된다")
    @Test
    void delete_softDeleteWorksForReadQueries() {
        // given
        SubwayStation station = repository.save(createStation(1001L, "2호선", "강남", "서울특별시", "강남구"));

        // when
        repository.delete(station);
        entityManager.flush();
        entityManager.clear();

        // then
        Number count = (Number) entityManager.createNativeQuery(
                        "select count(*) from subway_station where id = :id")
                .setParameter("id", station.getId())
                .getSingleResult();
        Object deletedAt = entityManager.createNativeQuery(
                        "select deleted_at from subway_station where id = :id")
                .setParameter("id", station.getId())
                .getSingleResult();

        assertThat(count.longValue()).isEqualTo(1L);
        assertThat(deletedAt).isNotNull();
        assertThat(repository.findAll())
                .extracting(SubwayStation::getId)
                .doesNotContain(station.getId());
    }

    @DisplayName("custom 조회(findByFilter)에서도 soft deleted 데이터는 제외된다")
    @Test
    void findByFilter_excludesSoftDeletedRows() {
        // given
        SubwayStation deleted = repository.save(createStation(2001L, "2호선", "선릉", "서울특별시", "강남구"));
        SubwayStation alive = repository.save(createStation(2002L, "2호선", "삼성", "서울특별시", "강남구"));
        repository.delete(deleted);
        entityManager.flush();
        entityManager.clear();

        // when
        List<SubwayStation> result = repository.findByFilter("서울특별시", "강남구");

        // then
        assertThat(result)
                .extracting(SubwayStation::getStationName)
                .contains("삼성")
                .doesNotContain("선릉");
    }

    private SubwayStation createStation(
            Long stationId,
            String lineName,
            String stationName,
            String city,
            String district
    ) {
        SubwayStation station = newSubwayStation();
        ReflectionTestUtils.setField(station, "stationId", stationId);
        ReflectionTestUtils.setField(station, "lineName", lineName);
        ReflectionTestUtils.setField(station, "stationName", stationName);
        ReflectionTestUtils.setField(station, "location", null);
        ReflectionTestUtils.setField(station, "address", BaseAddress.of(city, district));
        return station;
    }

    private SubwayStation newSubwayStation() {
        try {
            Constructor<SubwayStation> constructor = SubwayStation.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("SubwayStation 인스턴스 생성에 실패했습니다.", e);
        }
    }
}
