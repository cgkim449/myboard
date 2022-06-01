package com.cgkim.myboard.exception;

import lombok.Getter;

@Getter
public class InvalidJwtTokenException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidJwtTokenException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
