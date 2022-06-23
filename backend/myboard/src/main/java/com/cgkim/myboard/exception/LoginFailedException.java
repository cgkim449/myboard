package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

public class LoginFailedException extends BusinessException {
    public LoginFailedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
