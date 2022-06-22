package com.cgkim.myboard.exception;

import com.cgkim.myboard.exception.errorcode.ErrorCode;

public class QuestionNotFoundException extends BusinessException {
    public QuestionNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
