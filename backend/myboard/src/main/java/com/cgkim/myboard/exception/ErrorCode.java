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

    //Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "유효하지 않은 값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 오류입니다."),
    MAX_UPLOAD_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "C003", "최대 업로드 크기를 초과하였습니다."),
    GUEST_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "C004", "비밀번호가 틀렸습니다."),
    GUEST_PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "C005", "비밀번호를 입력해주세요."),

    //Authorization
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "A001", "올바르지 않은 토큰입니다."), // 401 인증실패
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A002", "로그아웃 되셨습니다."), // 401 인증실패
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "A003", "아이디 또는 비밀번호가 맞지 않습니다."),

    //Member
    USERNAME_DUPLICATE(HttpStatus.BAD_REQUEST, "M001", "이미 가입된 이메일입니다."),
    NICKNAME_DUPLICATE(HttpStatus.BAD_REQUEST, "M002", "이미 사용된 사용자 이름입니다."),

    //Board
    BOARD_INSERT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "B002", "게시물 작성에 실패했습니다."),

    //File
    ATTACH_NOT_FOUND(HttpStatus.NOT_FOUND, "F001", "해당 첨부파일이 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;
}