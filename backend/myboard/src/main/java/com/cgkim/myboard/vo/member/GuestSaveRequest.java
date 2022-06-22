package com.cgkim.myboard.vo.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GuestSaveRequest {
    private final String guestNickname;
    private final String guestPassword;
    private final String guestPasswordConfirm;
}
