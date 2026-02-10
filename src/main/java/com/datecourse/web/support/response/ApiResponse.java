package com.datecourse.web.support.response;

import com.datecourse.web.support.error.ErrorMessage;
import com.datecourse.web.support.error.ErrorType;

public record ApiResponse<T>(
        ResultType result,
        T data,
        ErrorMessage error
) {
    public static ApiResponse<Object> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <S> ApiResponse<S> success(S data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static <S> ApiResponse<S> error(S data, ErrorType errorData) {
        return new ApiResponse<>(ResultType.FAIL, null, ErrorMessage.from(errorData, data));
    }
}
