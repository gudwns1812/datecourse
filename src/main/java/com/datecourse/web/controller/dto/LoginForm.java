package com.datecourse.web.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm {

    @NotBlank
    private final String loginId;
    @NotBlank
    private final String password;
}
