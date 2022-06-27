package com.cgkim.myboard.controller;

import com.cgkim.myboard.jwt.JwtProvider;
import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.MemberService;
import com.cgkim.myboard.vo.member.LoginRequest;
import com.cgkim.myboard.vo.member.MemberVo;
import com.cgkim.myboard.vo.member.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.security.NoSuchAlgorithmException;

/**
 * 로그인 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class LoginController {

    private final MemberService memberService;

    private final JwtProvider jwtProvider;

    /**
     * 로그인
     *
     * @param loginRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws NoSuchAlgorithmException {

        MemberVo loginMember = memberService.login(loginRequest.getUsername(), loginRequest.getPassword()); // 로그인

        String token = jwtProvider.createToken(loginMember.getUsername()); // 토큰 생성

        return ResponseEntity.ok(
                new SuccessResponse()
                        .put("username", loginMember.getUsername())
                        .put("nickname", loginMember.getNickname())
                        .put("token", token));
    }
}
