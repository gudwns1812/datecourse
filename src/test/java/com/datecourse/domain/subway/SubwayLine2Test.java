package com.datecourse.domain.subway;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SubwayLine2Test {

    @Autowired
    SubwayLine2 subwayLine2;

    @Test
    void subwayLine2Test() {
        //given
        List<String> subwayNames = subwayLine2.getSubwayNames();
        //when
        Assertions.assertThat(subwayNames)
                .isNotEmpty();
        //then

        subwayNames.forEach(System.out::println);
    }
}
