package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;

public class NicknameDuplicateException extends BusinessException {
    public NicknameDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
