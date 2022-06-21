package com.cgkim.myboard.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;


public class GuestSaveRequestInvalidException extends BusinessException {
    @Getter
    private final BindingResult bindingResult;
    public GuestSaveRequestInvalidException(ErrorCode errorCode, BindingResult bindingResult) {
        super(errorCode);
        this.bindingResult = bindingResult;
    }
}
