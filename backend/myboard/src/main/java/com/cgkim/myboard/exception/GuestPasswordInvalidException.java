package com.cgkim.myboard.exception;

public class GuestPasswordInvalidException extends BusinessException {
    public GuestPasswordInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
