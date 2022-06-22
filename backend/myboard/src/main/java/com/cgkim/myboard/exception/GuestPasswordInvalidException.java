package com.cgkim.myboard.exception;

import com.cgkim.myboard.exception.errorcode.ErrorCode;

public class GuestPasswordInvalidException extends BusinessException {
    public GuestPasswordInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
