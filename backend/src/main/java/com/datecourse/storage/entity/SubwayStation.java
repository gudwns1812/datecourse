package com.datecourse.storage.entity;

import com.datecourse.domain.station.Station;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SubwayStation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long stationId;
    private String lineName;
    private String stationName;
    private Point location;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Station toDomain() {
        return Station.of(lineName, stationName, location);
    }
}
