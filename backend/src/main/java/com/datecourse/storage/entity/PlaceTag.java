package com.datecourse.storage.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "place_tag")
@Builder(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "update place_tag set deleted_at = now() where place_id = ? and tag_id = ?")
@SQLRestriction("deleted_at is null")
public class PlaceTag extends BaseTimeEntity {

    @EmbeddedId
    private PlaceTagId id;

    @MapsId("placeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public static PlaceTag create(Place place, Tag tag) {
        return PlaceTag.builder()
                .id(PlaceTagId.of(place.getId(), tag.getId()))
                .place(place)
                .tag(tag)
                .build();
    }
}
