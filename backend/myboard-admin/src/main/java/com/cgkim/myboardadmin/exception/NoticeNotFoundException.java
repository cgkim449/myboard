package com.cgkim.myboardadmin.exception;

import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;

/**
 * 존재하지 않는 공지사항
 */
public class NoticeNotFoundException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public NoticeNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
