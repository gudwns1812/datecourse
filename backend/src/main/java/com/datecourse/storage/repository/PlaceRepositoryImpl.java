package com.datecourse.storage.repository;

import static com.datecourse.storage.entity.QPlace.place;
import static com.datecourse.storage.entity.QPlaceTag.placeTag;
import static com.datecourse.storage.entity.QTag.tag;

import com.datecourse.storage.entity.Place;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Transactional
public class PlaceRepositoryImpl implements PlaceRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public List<Place> findCandidates(double latitude, double longitude, double radiusMeters) {
        List<Long> candidateIds = findCandidateIds(latitude, longitude, radiusMeters);
        if (candidateIds.isEmpty()) {
            return List.of();
        }

        return queryFactory.selectDistinct(place)
                .from(place)
                .leftJoin(place.placeTags, placeTag).fetchJoin()
                .leftJoin(placeTag.tag, tag).fetchJoin()
                .where(
                        place.id.in(candidateIds),
                        place.deletedAt.isNull()
                )
                .fetch();
    }

    private List<Long> findCandidateIds(double latitude, double longitude, double radiusMeters) {
        if (!Double.isFinite(latitude) || !Double.isFinite(longitude) || radiusMeters <= 0d) {
            return List.of();
        }

        List<?> rawIds = entityManager.createNativeQuery("""
                        select p.id
                        from place p
                        where p.deleted_at is null
                          and ST_DWithin(
                              p.location::geography,
                              ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
                              :radiusMeters,
                              false
                          )
                        """)
                .setParameter("longitude", longitude)
                .setParameter("latitude", latitude)
                .setParameter("radiusMeters", radiusMeters)
                .getResultList();

        return rawIds.stream()
                .map(Number.class::cast)
                .map(Number::longValue)
                .toList();
    }
}
