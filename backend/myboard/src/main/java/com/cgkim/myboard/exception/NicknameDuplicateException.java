package com.cgkim.myboard.exception;

import com.cgkim.myboard.exception.errorcode.ErrorCode;

public class NicknameDuplicateException extends BusinessException {
    public NicknameDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
