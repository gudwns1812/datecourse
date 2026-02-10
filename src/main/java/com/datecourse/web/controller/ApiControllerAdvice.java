package com.datecourse.web.controller;

import com.datecourse.web.support.error.CoreException;
import com.datecourse.web.support.error.ErrorType;
import com.datecourse.web.support.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ApiResponse<Object>> coreExceptionHandler(CoreException e) {
        ErrorType errorData = e.getErrorType();
        Object data = e.getData();

        log.debug("data = {}", data);

        switch (errorData.getLogLevel()) {
            case ERROR -> log.error("error = ", e);
            case WARN -> log.warn("warn = ", e);
            case DEBUG -> log.debug("debug error = ", e);
            default -> log.info("CoreException = ", e);
        }

        return new ResponseEntity<>(ApiResponse.error(data, errorData), errorData.getStatus());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Object> unExpectedExceptionHandler(RuntimeException e) {
        log.error("unexpected Error = ", e);

        return ApiResponse.error(null, ErrorType.INTERNAL_SERVER_ERROR);
    }
}
