package com.datecourse.domain.placegenerator;

import com.datecourse.domain.PlaceGenerator;
import com.datecourse.domain.subway.SubwayLine2;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RandomPlaceGenerator implements PlaceGenerator {
    @Override
    public String generate() {
        Random random = new Random();

        List<String> line2 = SubwayLine2.LINE2_ALL_WITH_BRANCHES;

        int randomNumber = random.nextInt(line2.size());
        log.info("randomNumber={}", randomNumber);
        return line2.get(randomNumber);
    }
}
