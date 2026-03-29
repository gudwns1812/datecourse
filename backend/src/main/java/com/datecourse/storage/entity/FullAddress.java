package com.datecourse.storage.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class FullAddress {
    private BaseAddress baseAddress;
    private String detail;        // 상세주소 (지하철역의 경우 출입구 번호나 층수)

    private FullAddress(String city, String district, String detail) {
        this.baseAddress = BaseAddress.of(city, district);
        this.detail = detail;
    }

    public String getAddressWithoutDetail() {
        return String.join(" ", baseAddress.getCity(), baseAddress.getDistrict());
    }

    public static FullAddress of(
            String city,
            String district,
            String detail
    ) {
        return new FullAddress(city, district, detail);
    }
}
