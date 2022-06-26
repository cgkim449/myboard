package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

/**
 * 존재하지 않는 답변
 */
public class AnswerNotFoundException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public AnswerNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
