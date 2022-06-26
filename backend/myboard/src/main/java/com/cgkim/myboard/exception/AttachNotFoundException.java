package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;

/**
 * 존재하지 않는 첨부파일
 */
public class AttachNotFoundException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public AttachNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
