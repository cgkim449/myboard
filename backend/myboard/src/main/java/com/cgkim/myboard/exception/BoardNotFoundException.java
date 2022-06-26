package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

/**
 * 존재하지 않는 게시물
 */
public class BoardNotFoundException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public BoardNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
