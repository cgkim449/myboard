package com.cgkim.myboard.exception;

public class NoAuthorizationException extends BusinessException {
    public NoAuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
