package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;

public class NoAuthorizationException extends BusinessException {
    public NoAuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
