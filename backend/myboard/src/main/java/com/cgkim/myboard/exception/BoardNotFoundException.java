package com.cgkim.myboard.exception;

import com.cgkim.myboard.exception.errorcode.ErrorCode;

public class BoardNotFoundException extends BusinessException {
    public BoardNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
