package com.cgkim.myboard.vo.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestSaveRequest {
    private String guestNickname;
    private String guestPassword;
    private String guestPasswordConfirm;

    @Builder
    public GuestSaveRequest(String guestNickname, String guestPassword, String guestPasswordConfirm) {
        this.guestNickname = guestNickname;
        this.guestPassword = guestPassword;
        this.guestPasswordConfirm = guestPasswordConfirm;
    }
}
