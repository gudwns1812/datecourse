package com.datecourse.domain.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class StationDto {

    @CsvBindByName(column = "철도운영기관명")
    public String organizationName;

    @CsvBindByName(column = "선명")
    public String lineName;

    @CsvBindByName(column = "역명")
    public String subwayName;

    @CsvBindByName(column = "지번주소")
    public String address;

    @CsvBindByName(column = "도로명주소")
    public String simpleAddress;
}
