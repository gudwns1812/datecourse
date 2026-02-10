package com.datecourse.web.support.error;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, ErrorCode.M001, "회원을 찾을 수 없습니다.", LogLevel.ERROR),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, ErrorCode.A001, "인증되지 않은 회원입니다.", LogLevel.WARN);

    private final HttpStatus status;
    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    ErrorType(HttpStatus status, ErrorCode code, String message, LogLevel logLevel) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }
}
