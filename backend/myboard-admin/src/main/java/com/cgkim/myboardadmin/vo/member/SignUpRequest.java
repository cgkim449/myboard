package com.cgkim.myboardadmin.vo.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private Long memberId;
    private String username;
    private String password;
    private String passwordConfirm;
    private String nickname;
}
