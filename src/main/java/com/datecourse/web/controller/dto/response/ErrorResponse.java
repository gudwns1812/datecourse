package com.datecourse.web.controller.dto.response;

import com.datecourse.error.ErrorCode;

public record ErrorResponse(
        ErrorCode code
) {
}
