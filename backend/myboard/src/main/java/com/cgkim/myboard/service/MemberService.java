package com.cgkim.myboard.service;


import com.cgkim.myboard.vo.member.MemberVo;
import com.cgkim.myboard.vo.member.SignUpRequest;
import java.security.NoSuchAlgorithmException;

public interface MemberService {
    String signUp(SignUpRequest signUpRequest) throws NoSuchAlgorithmException;
    MemberVo login(String username, String password) throws NoSuchAlgorithmException;
    MemberVo getMemberDetail(String username);
    Long getMemberId(String username);
}
