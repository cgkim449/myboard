package com.cgkim.myboardadmin.response;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 예외 발생시 응답 메시지
 */
@Getter
public class ErrorResponse {
    private final String errorCode;
    private final String errorMessage;
    private final List<FieldErrorDetail> fieldErrorDetails;

    @Builder
    public ErrorResponse(String errorCode, String errorMessage, List<FieldErrorDetail> fieldErrorDetails) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.fieldErrorDetails = initFieldErrorDetails(fieldErrorDetails); // fieldError 가 없는 경우 빈 배열
    }

    private List<FieldErrorDetail> initFieldErrorDetails(List<FieldErrorDetail> fieldErrorDetails) {
        return (fieldErrorDetails == null) ? new ArrayList<>() : fieldErrorDetails;
    }

    @Getter
    public static class FieldErrorDetail {
        private final String field;
        private final String fieldErrorMessage;

        @Builder
        public FieldErrorDetail(String field, String fieldErrorMessage) {
            this.field = field;
            this.fieldErrorMessage = fieldErrorMessage;
        }
    }
}