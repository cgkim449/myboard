package com.cgkim.myboard.controller;

import com.cgkim.myboard.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 인증 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class AuthController {

    @GetMapping("/auth")
    public ResponseEntity<SuccessResponse> auth(){

        return ResponseEntity.ok().build();
    }
}
