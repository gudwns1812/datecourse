package com.datecourse.support.response;

import com.datecourse.support.error.ErrorMessage;
import com.datecourse.support.error.ErrorType;

public record ApiResponse<T>(
        ResultType result,
        T data,
        ErrorMessage error
) {
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <S> ApiResponse<S> success(S data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static <S> ApiResponse<S> error(S data, ErrorType errorData) {
        return new ApiResponse<>(ResultType.FAIL, null, ErrorMessage.from(errorData, data));
    }
}
