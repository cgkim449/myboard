package com.cgkim.myboard.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 에러코드
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode { //TODO: properties 로 분리

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "유효하지 않은 값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 오류입니다."),
    MAX_UPLOAD_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "C003", "최대 업로드 크기를 초과하였습니다."),

    // User
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "U001", "이미 가입된 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "U002", "이미 사용된 사용자 이름입니다."),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "U003", "아이디 또는 비밀번호가 맞지 않습니다."),

    // Board
    BOARD_PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "B001", "비밀번호가 틀렸습니다."),
    BOARD_INSERT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "B002", "게시물 작성에 실패했습니다."),

    // Attach
    ATTACH_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "해당 첨부파일이 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;
}