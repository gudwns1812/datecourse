package com.datecourse.storage.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BaseAddress {
    private String city;          // 시/도 (예: 서울특별시, 경기도)
    private String district;      // 구/군 (예: 강남구, 수원시 팔달구)

    public static BaseAddress of(String city, String district) {
        return new BaseAddress(city, district);
    }
}
