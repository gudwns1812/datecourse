package com.datecourse.storage.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.locationtech.jts.geom.Point;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "update place set deleted_at = now() where id = ?")
@SQLRestriction("deleted_at is null")
public class Place extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String category;
    private String description;

    @Embedded
    private FullAddress address;

    private Point location;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<PlaceTag> placeTags = new LinkedHashSet<>();

    public static Place create(
            String name,
            String category,
            String description,
            FullAddress address,
            Point location
    ) {
        return Place.builder()
                .name(name)
                .category(category)
                .description(description)
                .address(address)
                .location(location)
                .build();
    }

    public Double getLatitude() {
        if (location == null || location.getCoordinate() == null) {
            return null;
        }
        return location.getCoordinate().getY();
    }

    public Double getLongitude() {
        if (location == null || location.getCoordinate() == null) {
            return null;
        }
        return location.getCoordinate().getX();
    }

    public List<String> getTagNames() {
        if (placeTags == null || placeTags.isEmpty()) {
            return List.of();
        }

        return placeTags.stream()
                .map(PlaceTag::getTag)
                .filter(Objects::nonNull)
                .map(Tag::getName)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }
}
