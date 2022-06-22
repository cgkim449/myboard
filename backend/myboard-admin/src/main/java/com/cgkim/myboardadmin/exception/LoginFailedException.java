package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;

public class LoginFailedException extends BusinessException {
    public LoginFailedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
