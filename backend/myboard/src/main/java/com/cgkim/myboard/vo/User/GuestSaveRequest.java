package com.cgkim.myboard.vo.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestSaveRequest {
    private String guestNickname;
    private String guestPassword;
    private String guestPasswordConfirm;
}
