package com.datecourse.web.controller.dto;

public record StatusResponseDto(
        boolean isLogin,
        String username
) {
}
