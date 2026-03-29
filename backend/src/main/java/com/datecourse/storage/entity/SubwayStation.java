package com.datecourse.storage.entity;

import com.datecourse.domain.station.Station;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.locationtech.jts.geom.Point;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "update subway_station set deleted_at = now() where id = ?")
@SQLRestriction("deleted_at is null")
public class SubwayStation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long stationId;
    private String lineName;
    private String stationName;
    private Point location;

    @Embedded
    private BaseAddress address;

    public Station toDomain() {
        return Station.of(lineName, stationName, location);
    }
}
