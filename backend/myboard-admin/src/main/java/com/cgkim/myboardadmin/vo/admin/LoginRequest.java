package com.cgkim.myboardadmin.vo.admin;

import lombok.Getter;

/**
 * 로그인 요청
 */
@Getter
public class LoginRequest {

    private final String username;

    private final String password;

    /**
     * 주입
     *
     * @param username
     * @param password
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
