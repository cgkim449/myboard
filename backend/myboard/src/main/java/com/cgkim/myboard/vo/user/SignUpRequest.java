package com.cgkim.myboard.vo.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private Long userId;
    private String username;
    private String password;
    private String nickname;
}
