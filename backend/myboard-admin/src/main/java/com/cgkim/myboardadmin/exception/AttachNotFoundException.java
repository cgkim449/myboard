package com.cgkim.myboardadmin.exception;

import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;

public class AttachNotFoundException extends BusinessException {
    public AttachNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
