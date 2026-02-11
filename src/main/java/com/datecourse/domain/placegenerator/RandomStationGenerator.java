package com.datecourse.domain.placegenerator;

import com.datecourse.domain.StationGenerator;
import com.datecourse.domain.station.StationLine2;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RandomStationGenerator implements StationGenerator {

    private final StationLine2 subwayLine2;

    @Override
    public String generate() {
        Random random = new Random();

        List<String> line2 = subwayLine2.getSubwayNames();

        int randomNumber = random.nextInt(line2.size());
        log.info("randomNumber={}", randomNumber);
        return line2.get(randomNumber);
    }
}
