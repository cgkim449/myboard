package com.cgkim.myboardadmin.exception;

import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;

public class NoticeNotFoundException extends BusinessException {
    public NoticeNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
