package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;

public class BoardNotFoundException extends BusinessException {
    public BoardNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
