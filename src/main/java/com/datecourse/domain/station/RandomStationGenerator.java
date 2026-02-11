package com.datecourse.domain.station;

import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RandomStationGenerator implements StationGenerator {

    @Override
    public Station generate(List<Station> stations) {
        Random random = new Random();
        int size = stations.size();
        int randomOffset = random.nextInt(size);

        return stations.stream()
                .skip(randomOffset)
                .findFirst()
                .orElseThrow();
    }
}
