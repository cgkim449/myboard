package com.cgkim.myboard.response;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 상태코드 4xx, 5xx 일때 응답 메시지
 */
@Getter
public class ErrorResponse {

    private final String errorCode;

    private final String errorMessage;

    private final List<FieldErrorDetail> fieldErrorDetails;

    /**
     * errorCode, errorMessage, fieldErrorDetails 주입
     * fieldError 가 없는 경우에는, 빈 배열([])로 응답하는 것이 목적
     *
     * @param errorCode
     * @param errorMessage
     * @param fieldErrorDetails
     */
    @Builder
    public ErrorResponse(String errorCode, String errorMessage, List<FieldErrorDetail> fieldErrorDetails) {

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.fieldErrorDetails = initFieldErrorDetails(fieldErrorDetails);
    }

    private List<FieldErrorDetail> initFieldErrorDetails(List<FieldErrorDetail> fieldErrorDetails) {

        return (fieldErrorDetails == null) ? new ArrayList<>() : fieldErrorDetails;
    }

    /**
     * 필드 에러
     */
    @Getter
    public static class FieldErrorDetail {

        private final String field;

        private final String fieldErrorMessage;

        /**
         * field, fieldErrorMessage 주입
         *
         * @param field
         * @param fieldErrorMessage
         */
        @Builder
        public FieldErrorDetail(String field, String fieldErrorMessage) {

            this.field = field;
            this.fieldErrorMessage = fieldErrorMessage;
        }
    }
}