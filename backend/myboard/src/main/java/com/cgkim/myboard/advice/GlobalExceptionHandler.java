package com.cgkim.myboard.advice;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.cgkim.myboard.exception.BoardInsertFailedException;
import com.cgkim.myboard.exception.BusinessException;
import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.exception.TokenInvalidException;
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

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;
    private final FileHandler fileHandler;

    /**
     * 최대한 여기서 모든 비즈니스 로직 예외처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> businessExceptionHandler(BusinessException exception) {
        log.error("handleBusinessException", exception);
        return ResponseEntity
                .status(exception.getErrorCode().getHttpStatus())
                .body(buildErrorResponse(exception.getErrorCode()));
    }

    /**
     * 게시물 DB에 insert 실패 시, 생성한 첨부파일이 있다면 그 파일을 삭제
     */
    @ExceptionHandler(BoardInsertFailedException.class)
    public ResponseEntity<ErrorResponse> boardInsertFailedExceptionHandler(BoardInsertFailedException exception) {
        log.error("handleBoardInsertFailedException", exception);

        fileHandler.deleteFiles(exception.getAttachSaveList()); // 생성했던 파일 삭제
        return ResponseEntity
                .status(exception.getErrorCode().getHttpStatus())
                .body(buildErrorResponse(exception.getErrorCode()));
    }

    /**
     * 민료된 토큰 = 로그아웃
     * @return TODO: redirect 상태코드 location 주면됨 참고
     */
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> TokenExpiredExceptionHandler(TokenExpiredException exception) {
        log.error("TokenExpiredExceptionException", exception);
        return ResponseEntity
                .status(ErrorCode.TOKEN_EXPIRED.getHttpStatus())
                .body(buildErrorResponse(ErrorCode.TOKEN_EXPIRED));
    }

    /**
     * 유효하지 않은 토큰
     * @return 401
     */
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorResponse> JWTVerificationExceptionHandler(JWTVerificationException exception) {
        log.error("JWTVerificationExceptionException", exception);
        return ResponseEntity
                .status(ErrorCode.TOKEN_INVALID.getHttpStatus())
                .body(buildErrorResponse(ErrorCode.TOKEN_INVALID));
    }

    /**
     * 바인딩 예외 처리
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
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException exception) {
        log.error("handleMaxUploadSizeExceededException", exception);
        return ResponseEntity
                .status(ErrorCode.MAX_UPLOAD_SIZE_EXCEEDED.getHttpStatus())
                .body(buildErrorResponse(ErrorCode.MAX_UPLOAD_SIZE_EXCEEDED));
    }

    /**
     * 모든 예외를 처리
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
