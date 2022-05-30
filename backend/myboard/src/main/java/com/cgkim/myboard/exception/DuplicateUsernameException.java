package com.cgkim.myboard.exception;

public class DuplicateUsernameException extends BusinessException {
    public DuplicateUsernameException(ErrorCode errorCode) {
        super(errorCode);
    }
}
