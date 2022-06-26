package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

/**
 * 존재하지 않는 회원
 */
public class MemberNotFoundException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
