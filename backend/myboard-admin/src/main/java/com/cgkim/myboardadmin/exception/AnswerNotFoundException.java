package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;

/**
 * 답변이 존재하지 않음
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
