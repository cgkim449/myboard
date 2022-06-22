package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;

public class GuestPasswordInvalidException extends BusinessException {
    public GuestPasswordInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
