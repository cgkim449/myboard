package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

public class GuestPasswordMismatchException extends BusinessException{
    public GuestPasswordMismatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
