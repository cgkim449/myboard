package com.cgkim.myboard.exception;

public class GuestPasswordMismatchException extends BusinessException{
    public GuestPasswordMismatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
