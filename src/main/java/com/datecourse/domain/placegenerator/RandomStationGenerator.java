package com.datecourse.domain.placegenerator;

import com.datecourse.domain.StationGenerator;
import com.datecourse.domain.station.Station;
import com.datecourse.domain.station.StationLine2;

import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RandomStationGenerator implements StationGenerator {

    private final StationLine2 stationLine2;

    @Override
    public Station generate() {
        Random random = new Random();

        int size = stationLine2.getSize();

        int randomNumber = random.nextInt(size);
        log.info("randomNumber={}", randomNumber);
        return stationLine2.getStationByOffset(randomNumber);
    }
}
