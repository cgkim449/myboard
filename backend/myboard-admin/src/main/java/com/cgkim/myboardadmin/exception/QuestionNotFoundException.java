package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;

public class QuestionNotFoundException extends BusinessException {
    public QuestionNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
