package com.cgkim.myboard.vo.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 익명 글/댓글 등록시 별명/비밀번호 저장 요청
 */
@Getter
@RequiredArgsConstructor
public class GuestSaveRequest {

    private final String guestNickname;

    private final String guestPassword;

    private final String guestPasswordConfirm;
}
