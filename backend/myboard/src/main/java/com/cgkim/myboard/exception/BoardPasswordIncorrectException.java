package com.cgkim.myboard.exception;

public class BoardPasswordIncorrectException extends BusinessException{
    public BoardPasswordIncorrectException(ErrorCode errorCode) {
        super(errorCode);
    }
}
