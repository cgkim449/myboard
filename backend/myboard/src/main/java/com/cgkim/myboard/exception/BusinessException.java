package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;
import lombok.Getter;

/**
 * 비즈니스 로직 수행 중 발생한 에러
 * - 목적: 비즈니스 로직과 예외 처리 코드를 분리
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
