package com.cgkim.myboard.exception;

public class AttachNotFoundException extends BusinessException {
    public AttachNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
