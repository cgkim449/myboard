package com.cgkim.myboard.exception;

public class UsernameDuplicateException extends BusinessException {
    public UsernameDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
