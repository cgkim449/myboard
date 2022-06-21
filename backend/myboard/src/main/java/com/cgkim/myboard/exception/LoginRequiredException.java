package com.cgkim.myboard.exception;

public class LoginRequiredException extends BusinessException {
    public LoginRequiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
