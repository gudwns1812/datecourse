package com.datecourse.web.support.error;

import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class CoreException extends RuntimeException {
    private final ErrorType errorType;
    private final Object data;

    public CoreException(ErrorType errorType, @Nullable Object data) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = data;
    }
}
