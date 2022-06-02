package com.cgkim.myboard.exception;

import lombok.Getter;

@Getter
public class TokenInvalidException extends RuntimeException {
    private final ErrorCode errorCode;

    public TokenInvalidException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
