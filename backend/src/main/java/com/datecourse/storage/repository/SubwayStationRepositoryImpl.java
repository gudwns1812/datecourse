package com.datecourse.storage.repository;

import static com.datecourse.storage.entity.QSubwayStation.subwayStation;

import com.datecourse.storage.entity.SubwayStation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Transactional
public class SubwayStationRepositoryImpl implements SubwayStationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SubwayStation> findByFilter(String city, String district) {
        return queryFactory.selectFrom(subwayStation)
                .where(
                        eqCity(city),
                        eqDistrict(district)
                )
                .fetch();
    }

    private BooleanExpression eqCity(String city) {
        if (city == null || city.isBlank()) {
            return null;
        }

        return subwayStation.address.city.eq(city);
    }

    private BooleanExpression eqDistrict(String district) {
        if (district == null || district.isBlank()) {
            return null;
        }

        return subwayStation.address.district.eq(district);
    }
}
