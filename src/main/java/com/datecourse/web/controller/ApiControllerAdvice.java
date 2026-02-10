package com.datecourse.web.controller;

import com.datecourse.web.support.error.CoreException;
import com.datecourse.web.support.error.ErrorType;
import com.datecourse.web.support.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ApiResponse<Object>> coreExceptionHandler(CoreException e) {
        ErrorType errorData = e.getErrorType();
        Object data = e.getData();

        return new ResponseEntity<>(ApiResponse.error(data, errorData), errorData.getStatus());
    }
}
