package com.cgkim.myboard.exception;

import com.cgkim.myboard.exception.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
