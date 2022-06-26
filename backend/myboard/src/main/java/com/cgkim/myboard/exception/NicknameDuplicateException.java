package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

/**
 * 회원가입시 닉네임 중복 예외
 */
public class NicknameDuplicateException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public NicknameDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
