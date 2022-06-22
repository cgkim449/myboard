package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;

public class UsernameDuplicateException extends BusinessException {
    public UsernameDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
