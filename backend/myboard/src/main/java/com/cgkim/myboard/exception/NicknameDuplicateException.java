package com.cgkim.myboard.exception;

public class NicknameDuplicateException extends BusinessException {
    public NicknameDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
