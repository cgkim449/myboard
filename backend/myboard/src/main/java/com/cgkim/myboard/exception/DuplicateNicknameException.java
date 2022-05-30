package com.cgkim.myboard.exception;

public class DuplicateNicknameException extends BusinessException {
    public DuplicateNicknameException(ErrorCode errorCode) {
        super(errorCode);
    }
}
