package com.datecourse.storage.entity;

import jakarta.persistence.Embeddable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class FullAddress {
    private BaseAddress baseAddress;
    private String roadName;      // 도로명 (예: 강남대로, 아차산로)
    private String buildingNumber;// 건물번호 (예: 396, 113)
    private String detail;        // 상세주소 (지하철역의 경우 출입구 번호나 층수)
    private String zipCode;       // 우편번호 (5자리)

    private FullAddress(String city, String district, String roadName, String buildingNumber, String detail,
                        String zipCode) {
        this.baseAddress = BaseAddress.of(city, district);
        this.roadName = roadName;
        this.buildingNumber = buildingNumber;
        this.detail = detail;
        this.zipCode = zipCode;
    }

    public String getAddressWithoutDetail() {
        return String.join(" ", baseAddress.getCity(), baseAddress.getDistrict(), roadName, buildingNumber);
    }

    public static FullAddress from(String simpleAddress) {
        if (simpleAddress == null || simpleAddress.isBlank()) {
            return null;
        }

        String regex = "^(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+([지하]*\\d+)(.*)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(simpleAddress.trim());

        if (matcher.find()) {
            String city = matcher.group(1);
            String district = matcher.group(2);
            String roadName = matcher.group(3);
            String buildingNumber = matcher.group(4);
            String detail = matcher.group(5).trim();

            return new FullAddress(city, district, roadName, buildingNumber, detail, "");
        }

        String[] parts = simpleAddress.split(" ", 4);
        return new FullAddress(
                parts.length > 0 ? parts[0] : "",
                parts.length > 1 ? parts[1] : "",
                parts.length > 2 ? parts[2] : "",
                parts.length > 3 ? parts[3] : "",
                "", ""
        );
    }
}
