package com.datecourse.service.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginForm(
        @NotEmpty(message = "ID를 입력해주세요.")
        String loginId,
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        String password
) {
}
