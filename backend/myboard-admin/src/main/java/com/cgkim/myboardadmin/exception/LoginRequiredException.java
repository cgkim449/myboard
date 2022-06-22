package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;

public class LoginRequiredException extends BusinessException {
    public LoginRequiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
