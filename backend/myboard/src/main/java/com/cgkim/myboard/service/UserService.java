package com.cgkim.myboard.service;

import com.cgkim.myboard.vo.user.SignUpRequest;
import com.cgkim.myboard.vo.user.SignUpResponse;
import com.cgkim.myboard.vo.user.UserVo;

public interface UserService {
    SignUpResponse signUp(SignUpRequest signUpRequest);
}
