package com.datecourse.domain.station;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RandomStationGenerator implements StationGenerator {

    @Override
    public Station generate(List<Station> stations) {
        var copy = new ArrayList<>(stations);

        Collections.shuffle(copy);
        return copy.getFirst();
    }
}
