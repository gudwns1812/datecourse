package com.datecourse.global.config;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.datecourse.domain.dto.StationDto;
import com.datecourse.domain.station.Station;
import com.datecourse.repository.StationMemoryRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StationInit {

    private final StationMemoryRepository repository;
    private final ResourceLoader resourceLoader;

    @PostConstruct
    public void init() {
        Resource resource = resourceLoader.getResource(
                "classpath:Line_number_2_address_data_20241022.csv");

        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            List<StationDto> stationResponses = new CsvToBeanBuilder<StationDto>(reader)
                    .withType(StationDto.class)
                    .build()
                    .parse();

            for (StationDto response : stationResponses) {
                repository.save(new Station(response.lineName, response.subwayName, response.simpleAddress));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
