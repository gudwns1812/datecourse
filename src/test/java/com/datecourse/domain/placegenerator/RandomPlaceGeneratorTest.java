package com.datecourse.domain.placegenerator;

import com.datecourse.domain.PlaceGenerator;
import org.junit.jupiter.api.Test;

class RandomPlaceGeneratorTest {

    PlaceGenerator generator = new RandomPlaceGenerator();

    @Test
    void generateTest() {
        //given
        String generate = generator.generate();
        //when
        System.out.println(generate);
        //then
    }
}
