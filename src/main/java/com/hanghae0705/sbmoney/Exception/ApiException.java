package com.hanghae0705.sbmoney.Exception;

import lombok.Getter;

@Getter
public enum ApiException implements BaseExceptionType{

    DUPLICATED_USER(1001, 500, "이미 존재하는 사용자 아이디입니다."),
    NOT_EXIST_USER(1002, 500, "해당 아이디가 없습니다."),
    SHORT_PASSWORD(1003,203,"비밀번호는 4자 이상입니다."),
    SAME_PASSWORD(1004, 203, "유저 이름과 같은 비밀번호는 사용할 수 없습니다."),
    REGEXP_PASSWORD(1005, 203, "비밀번호는 영문 대소문자, 숫자만 사용 가능합니다");

    private int errorCode;
    private int httpStatus;
    private String errorMessage;

    ApiException(int errorCode, int httpStatus, String errorMessage){
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
