package com.cgkim.myboard.controller;

import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.MemberService;
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
 * 회원가입 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class SignUpController {

    private final MemberService memberService;


    /**
     * 회원가입
     *
     * @param signUpRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) throws NoSuchAlgorithmException {

        String username = memberService.signUp(signUpRequest);

        return ResponseEntity.created(URI.create("/members/" + username)).body(new SuccessResponse());
    }
}
