package com.datecourse.web.controller.dto;

import java.util.List;

public record FiltersDto(
        List<String> lines,
        List<String> cities,
        List<String> districts
) {
}
