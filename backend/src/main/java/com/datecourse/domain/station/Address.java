package com.datecourse.domain.station;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class Address {
    private final String city;          // 시/도 (예: 서울특별시, 경기도)
    private final String district;      // 구/군 (예: 강남구, 수원시 팔달구)
    private final String roadName;      // 도로명 (예: 강남대로, 아차산로)
    private final String buildingNumber;// 건물번호 (예: 396, 113)
    private final String detail;        // 상세주소 (지하철역의 경우 출입구 번호나 층수)
    private final String zipCode;       // 우편번호 (5자리)

    private Address(String city, String district, String roadName, String buildingNumber, String detail, String zipCode) {
        this.city = city;
        this.district = district;
        this.roadName = roadName;
        this.buildingNumber = buildingNumber;
        this.detail = detail;
        this.zipCode = zipCode;
    }

    public String getAddressWithoutDetail() {
        return String.join(" ", city, district, roadName, buildingNumber);
    }

    public static Address from(String simpleAddress) {
        if (simpleAddress == null || simpleAddress.isBlank()) {
            return null;
        }

        String regex = "^(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+([지하]*\\d+)(.*)$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(simpleAddress.trim());

        if (matcher.find()) {
            String city = matcher.group(1);
            String district = matcher.group(2);
            String roadName = matcher.group(3);
            String buildingNumber = matcher.group(4);
            String detail = matcher.group(5).trim();

            return new Address(city, district, roadName, buildingNumber, detail, "");
        }

        String[] parts = simpleAddress.split(" ", 4);
        return new Address(
                parts.length > 0 ? parts[0] : "",
                parts.length > 1 ? parts[1] : "",
                parts.length > 2 ? parts[2] : "",
                parts.length > 3 ? parts[3] : "",
                "", ""
        );
    }
}
