package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

public class NoAuthorizationException extends BusinessException {
    public NoAuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
