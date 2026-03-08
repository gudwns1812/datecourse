package com.datecourse.web.support.error;

public record ErrorMessage(
        ErrorCode code,
        String message,
        Object data
) {
    public static ErrorMessage from(ErrorType errorType, Object data) {
        return new ErrorMessage(errorType.getCode(), errorType.getMessage(), data);
    }
}
