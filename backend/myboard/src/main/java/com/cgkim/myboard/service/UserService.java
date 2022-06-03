package com.cgkim.myboard.service;

import com.cgkim.myboard.vo.user.SignUpRequest;
import com.cgkim.myboard.vo.user.UserVo;

import java.security.NoSuchAlgorithmException;

public interface UserService {
    String signUp(SignUpRequest signUpRequest) throws NoSuchAlgorithmException;
    UserVo login(String username, String password) throws NoSuchAlgorithmException;
    UserVo getUserDetail(String username);
    Object getUserId(String username);
}
