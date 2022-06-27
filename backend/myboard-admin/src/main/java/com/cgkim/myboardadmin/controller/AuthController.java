package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 관리자 인증 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AuthController {

    /**
     * 관리자 인증(preHandle 에서 토큰으로 검증)
     * - 페이지 이동할때마다 vue 네비게이션 가드에서 이 url 을 요청
     * - 그래서 인증 실패시 페이지 이동 불가
     *
     * @return ResponseEntity<SuccessResponse>
     */
    @GetMapping("/auth")
    public ResponseEntity<SuccessResponse> auth() {

        return ResponseEntity.ok(new SuccessResponse());
    }
}
