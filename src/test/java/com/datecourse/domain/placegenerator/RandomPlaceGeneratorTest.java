package com.datecourse.domain.placegenerator;

import com.datecourse.domain.StationGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RandomPlaceGeneratorTest {

    @Autowired
    StationGenerator generator;

    @Test
    void generateTest() {
        //given
        String generate = generator.generate();
        //when
        System.out.println(generate);
        //then
    }
}
