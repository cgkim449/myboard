package com.cgkim.myboard.exception;

import com.cgkim.myboard.exception.errorcode.ErrorCode;

public class LoginRequiredException extends BusinessException {
    public LoginRequiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
