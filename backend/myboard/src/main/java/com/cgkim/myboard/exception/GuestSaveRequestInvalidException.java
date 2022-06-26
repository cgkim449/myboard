package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.validation.BindingResult;

/**
 * 익명 글 또는 비밀글 작성시, 유효하지 않은 별명 및 비밀번호
 */
public class GuestSaveRequestInvalidException extends BusinessException {

    @Getter
    private final BindingResult bindingResult;

    /**
     * 에러코드 주입
     *
     * @param errorCode
     * @param bindingResult
     */
    public GuestSaveRequestInvalidException(ErrorCode errorCode, BindingResult bindingResult) {
        super(errorCode);
        this.bindingResult = bindingResult;
    }
}
