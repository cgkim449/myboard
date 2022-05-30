package com.cgkim.myboard.exception;

public class LoginFailedException extends BusinessException {
    public LoginFailedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
