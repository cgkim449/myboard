package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

public class UsernameDuplicateException extends BusinessException {
    public UsernameDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
