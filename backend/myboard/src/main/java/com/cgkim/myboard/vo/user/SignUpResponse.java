package com.cgkim.myboard.vo.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SignUpResponse {
    private Long userId;
    private String username;
    private String nickname;

    @Builder
    public SignUpResponse(Long userId, String username, String nickname) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
    }
}
