package com.hanghae0705.sbmoney.Exception;

import lombok.Getter;

public class ApiRequestException extends IllegalArgumentException {

    @Getter
    private final BaseExceptionType exceptionType;

    public ApiRequestException(BaseExceptionType exceptionType) {
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }
}