package com.datecourse.domain.station;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.datecourse.domain.dto.StationDto;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StationLine2 {

    private final ResourceLoader resourceLoader;
    private List<StationDto> subwayLine2 = new ArrayList<>();

    @PostConstruct
    public void init() {
        Resource resource = resourceLoader.getResource(
                "classpath:Line_number_2_address_data_20241022.csv");

        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            subwayLine2 = new CsvToBeanBuilder<StationDto>(reader)
                    .withType(StationDto.class)
                    .build()
                    .parse();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getSize() {
        return subwayLine2.size();
    }

    public Station getStationByOffset(int randomNumber) {
        StationDto stationDto = subwayLine2.stream()
                .skip(randomNumber)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("로직에 오류가 있습니다."));

        return new Station(stationDto.lineName, stationDto.subwayName, stationDto.simpleAddress);
    }
}
