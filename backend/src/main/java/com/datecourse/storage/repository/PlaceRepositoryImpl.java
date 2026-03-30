package com.datecourse.storage.repository;

import static com.datecourse.storage.entity.QPlace.place;
import static com.datecourse.storage.entity.QPlaceTag.placeTag;
import static com.datecourse.storage.entity.QTag.tag;

import com.datecourse.storage.entity.Place;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Transactional
public class PlaceRepositoryImpl implements PlaceRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Place> findCandidates(
            double latitude,
            double longitude,
            double radiusMeters,
            String query,
            String category
    ) {
        return queryFactory.selectDistinct(place)
                .from(place)
                .leftJoin(place.placeTags, placeTag).fetchJoin()
                .leftJoin(placeTag.tag, tag).fetchJoin()
                .where(
                        withinRadius(latitude, longitude, radiusMeters),
                        eqCategory(category),
                        matchQuery(query)
                )
                .fetch();
    }

    private BooleanExpression withinRadius(double latitude, double longitude, double radiusMeters) {
        if (!Double.isFinite(latitude) || !Double.isFinite(longitude) || radiusMeters <= 0d) {
            return null;
        }

        return Expressions.booleanTemplate(
                "ST_DWithin({0}::geography, ST_SetSRID(ST_MakePoint({1}, {2}), 4326)::geography, {3})",
                place.location,
                longitude,
                latitude,
                radiusMeters
        );
    }

    private BooleanExpression eqCategory(String category) {
        if (category == null || category.isBlank()) {
            return null;
        }

        return place.category.eq(category);
    }

    private BooleanExpression matchQuery(String query) {
        if (query == null || query.isBlank()) {
            return null;
        }

        return place.name.containsIgnoreCase(query)
                .or(place.category.containsIgnoreCase(query))
                .or(tag.name.containsIgnoreCase(query));
    }
}
