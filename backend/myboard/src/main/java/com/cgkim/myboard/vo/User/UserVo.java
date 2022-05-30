package com.cgkim.myboard.vo.user;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserVo {
    private Long userId;
    private String username;
    private String password;
    private String nickname;
    private Date userRegisterDate;
    private Date userUpdateDate;
}
