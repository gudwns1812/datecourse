package com.datecourse.storage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
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
@Table(name = "tag")
@Builder(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "update tag set deleted_at = now() where id = ?")
@SQLRestriction("deleted_at is null")
public class Tag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<PlaceTag> placeTags = new LinkedHashSet<>();

    public static Tag create(String name) {
        return Tag.builder()
                .name(name)
                .build();
    }
}
