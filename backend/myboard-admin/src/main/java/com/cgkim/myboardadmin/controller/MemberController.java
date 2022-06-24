package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.jwt.JwtProvider;
import com.cgkim.myboardadmin.response.SuccessResponse;
import com.cgkim.myboardadmin.service.MemberService;
import com.cgkim.myboardadmin.validator.LoginRequestValidator;
import com.cgkim.myboardadmin.validator.SignUpRequestValidator;
import com.cgkim.myboardadmin.vo.member.LoginRequest;
import com.cgkim.myboardadmin.vo.member.MemberVo;
import com.cgkim.myboardadmin.vo.member.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/members")
public class MemberController {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    /**
     * 회원가입
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) throws NoSuchAlgorithmException {

        String username = memberService.signUp(signUpRequest);

        return ResponseEntity.created(URI.create("/members/" + username)).body(new SuccessResponse());
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody @Valid LoginRequest loginRequest) throws NoSuchAlgorithmException {
        // 로그인
        MemberVo loginMember = memberService.login(loginRequest.getUsername(), loginRequest.getPassword());
        // 토큰 생성
        String token = jwtProvider.createToken(loginMember.getUsername(), false);

        return ResponseEntity.ok(
                new SuccessResponse()
                        .put("username", loginMember.getUsername())
                        .put("nickname", loginMember.getNickname())
                        .put("token", token));
    }
}
