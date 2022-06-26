package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

/**
 * 유효하지 않은 익명 글 또는 댓글 비밀번호
 */
public class GuestPasswordInvalidException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public GuestPasswordInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
