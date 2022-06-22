package com.cgkim.myboardadmin.exception;

import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class TokenInvalidException extends RuntimeException {
    private final ErrorCode errorCode;

    public TokenInvalidException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
