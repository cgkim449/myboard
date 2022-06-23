package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

public class NoticeNotFoundException extends BusinessException {
    public NoticeNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
