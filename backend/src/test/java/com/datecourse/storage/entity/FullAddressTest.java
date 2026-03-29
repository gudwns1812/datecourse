package com.datecourse.storage.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FullAddressTest {

    @DisplayName("FullAddress는 시/군/구와 상세주소만 보관한다")
    @Test
    void of_createsFullAddressWithExplicitFields() {
        // given
        FullAddress address = FullAddress.of(
                "경기도",
                "안양시 동안구",
                "2층"
        );

        // then
        assertThat(address.getBaseAddress().getCity()).isEqualTo("경기도");
        assertThat(address.getBaseAddress().getDistrict()).isEqualTo("안양시 동안구");
        assertThat(address.getDetail()).isEqualTo("2층");
        assertThat(address.getAddressWithoutDetail()).isEqualTo("경기도 안양시 동안구");
    }
}
