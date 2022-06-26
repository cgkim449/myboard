package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.jwt.JwtProvider;
import com.cgkim.myboardadmin.response.SuccessResponse;
import com.cgkim.myboardadmin.service.AdminService;
import com.cgkim.myboardadmin.vo.admin.AdminVo;
import com.cgkim.myboardadmin.vo.admin.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

/**
 * 관리자 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final JwtProvider jwtProvider;

    /**
     * 로그인
     *
     * @param loginRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody @Valid LoginRequest loginRequest) throws NoSuchAlgorithmException {

        AdminVo loginAdmin = adminService.login(loginRequest.getUsername(), loginRequest.getPassword());

        String token = jwtProvider.createToken(loginAdmin.getUsername(), true);

        return ResponseEntity.ok(
                new SuccessResponse()
                        .put("username", loginAdmin.getUsername())
                        .put("nickname", loginAdmin.getNickname())
                        .put("token", token));
    }


    /**
     * 사용자가 관리자인지 아닌지 검증하는 것이 목적(preHandler 에서 토큰으로 검증함)
     *
     * @return ResponseEntity<SuccessResponse>
     */
    //TODO: 메서드, uri 이름 바꾸기
    @GetMapping("/check")
    public ResponseEntity<SuccessResponse> check() {

        return ResponseEntity.ok(new SuccessResponse());
    }
}
