package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

/**
 * 로그인 실패
 */
public class LoginFailedException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public LoginFailedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
