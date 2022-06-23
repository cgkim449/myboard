package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

public class AttachNotFoundException extends BusinessException {
    public AttachNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
