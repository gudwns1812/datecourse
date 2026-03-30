package com.datecourse.storage.entity;

import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlaceTagId implements Serializable {

    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "tag_id")
    private Long tagId;

    public static PlaceTagId of(Long placeId, Long tagId) {
        return new PlaceTagId(placeId, tagId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceTagId that = (PlaceTagId) o;
        return Objects.equals(placeId, that.placeId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, tagId);
    }
}
