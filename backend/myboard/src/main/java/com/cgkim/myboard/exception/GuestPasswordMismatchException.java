package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

/**
 * 익명 글 또는 댓글 비밀번호 불일치
 */
public class GuestPasswordMismatchException extends BusinessException{

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public GuestPasswordMismatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
