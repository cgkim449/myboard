package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

/**
 * 회원가입시 username 중복 에러
 */
public class UsernameDuplicateException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public UsernameDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
