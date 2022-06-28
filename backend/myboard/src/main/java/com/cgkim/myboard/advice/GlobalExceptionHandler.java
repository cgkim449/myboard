package com.cgkim.myboard.advice;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.cgkim.myboard.exception.BusinessException;
import com.cgkim.myboard.exception.InsertFailedException;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.exception.GuestSaveRequestInvalidException;
import com.cgkim.myboard.response.ErrorResponse;
import com.cgkim.myboard.util.FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.ArrayList;
import java.util.List;

/**
 * 역할: 모든 예외를 처리
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private final FileHandler fileHandler;

    /**
     * - 최대한 여기서 모든 비즈니스 로직 예외처리
     * - 목적: 비즈니스 로직과 예외 처리 코드를 분리
     *
     * @param exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> businessExceptionHandler(BusinessException exception) {

        log.error("handleBusinessException", exception);

        return ResponseEntity
                .status(exception.getErrorCode().getHttpStatus())
                .body(buildErrorResponse(exception.getErrorCode()));
    }

    /**
     * 게시물/질문 등록 시 DB에 insert 실패한 경우, 생성한 물리적 파일이 있었다면 그 파일을 삭제
     *
     * @param exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(InsertFailedException.class)
    public ResponseEntity<ErrorResponse> insertFailedExceptionHandler(InsertFailedException exception) {

        log.error("handleInsertFailedException", exception);

        fileHandler.deleteFiles(exception.getAttachSaveList()); //생성했던 파일 삭제

        return ResponseEntity
                .status(exception.getErrorCode().getHttpStatus())
                .body(buildErrorResponse(exception.getErrorCode()));
    }

    /**
     * 익명 게시글/댓글 비밀번호가 유효하지 않음
     *
     * @param exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(GuestSaveRequestInvalidException.class)
    public ResponseEntity<ErrorResponse> GuestSaveRequestInvalidExceptionHandler(GuestSaveRequestInvalidException exception) {

        log.error("handleGuestSaveRequestInvalidException", exception);

        List<ErrorResponse.FieldErrorDetail> fieldErrorDetails = getFieldErrorDetails(exception.getBindingResult());

        return ResponseEntity
                .status(exception.getErrorCode().getHttpStatus())
                .body(buildErrorResponse(exception.getErrorCode(), fieldErrorDetails));
    }

    /**
     * 만료된 토큰
     *
     * @param exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> TokenExpiredExceptionHandler(TokenExpiredException exception) {

        log.error("handleTokenExpiredException", exception);

        return ResponseEntity
                .status(ErrorCode.TOKEN_EXPIRED.getHttpStatus())
                .body(buildErrorResponse(ErrorCode.TOKEN_EXPIRED));
    }

    /**
     * 유효하지 않은 토큰
     *
     * @param exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorResponse> JWTVerificationExceptionHandler(JWTVerificationException exception) {

        log.error("handleJWTVerificationException", exception);

        return ResponseEntity
                .status(ErrorCode.TOKEN_INVALID.getHttpStatus())
                .body(buildErrorResponse(ErrorCode.TOKEN_INVALID));
    }

    /**
     * 바인딩 예외 처리
     *
     * @param exception
     * @param bindingResult
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindExceptionHandler(BindException exception, BindingResult bindingResult) {

        log.error("handleBindException", exception);

        List<ErrorResponse.FieldErrorDetail> fieldErrorDetails = getFieldErrorDetails(bindingResult);

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getHttpStatus())
                .body(buildErrorResponse(ErrorCode.INVALID_INPUT_VALUE, fieldErrorDetails));
    }

    /**
     * 최대 업로드 크기 초과 예외 처리
     *
     * @param exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException exception) {

        log.error("handleMaxUploadSizeExceededException", exception);

        return ResponseEntity
                .status(ErrorCode.MAX_UPLOAD_SIZE_EXCEEDED.getHttpStatus())
                .body(buildErrorResponse(ErrorCode.MAX_UPLOAD_SIZE_EXCEEDED));
    }

    /**
     * 그 외 모든 예외를 처리
     *
     * @param exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception exception) {

        log.error("handleException", exception);

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(buildErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private ErrorResponse buildErrorResponse(ErrorCode errorCode) {

        return ErrorResponse.builder()
                .errorCode(errorCode.getErrorCode())
                .errorMessage(errorCode.getErrorMessage())
                .build();
    }

    private ErrorResponse buildErrorResponse(ErrorCode errorCode, List<ErrorResponse.FieldErrorDetail> fieldErrorDetails) {

        return ErrorResponse.builder()
                .errorCode(errorCode.getErrorCode())
                .errorMessage(errorCode.getErrorMessage())
                .fieldErrorDetails(fieldErrorDetails)
                .build();
    }

    private List<ErrorResponse.FieldErrorDetail> getFieldErrorDetails(BindingResult bindingResult) {

        List<ErrorResponse.FieldErrorDetail> fieldErrorDetails = new ArrayList<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String field = fieldError.getField();
            String fieldErrorMessage = messageSource.getMessage(fieldError, null);

            fieldErrorDetails.add(
                    ErrorResponse.FieldErrorDetail.builder()
                            .field(field)
                            .fieldErrorMessage(fieldErrorMessage)
                            .build());
        }

        return fieldErrorDetails;
    }
}
