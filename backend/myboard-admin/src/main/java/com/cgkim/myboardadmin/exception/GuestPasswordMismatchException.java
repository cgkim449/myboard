package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;

public class GuestPasswordMismatchException extends BusinessException{
    public GuestPasswordMismatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
