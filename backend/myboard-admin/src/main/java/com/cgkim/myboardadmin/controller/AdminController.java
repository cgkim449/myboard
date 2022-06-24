package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.jwt.JwtProvider;
import com.cgkim.myboardadmin.response.SuccessResponse;
import com.cgkim.myboardadmin.service.AdminService;
import com.cgkim.myboardadmin.validator.LoginRequestValidator;
import com.cgkim.myboardadmin.vo.admin.AdminVo;
import com.cgkim.myboardadmin.vo.member.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final JwtProvider jwtProvider;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody @Valid LoginRequest loginRequest) throws NoSuchAlgorithmException {
        // 로그인
        AdminVo loginAdmin = adminService.login(loginRequest.getUsername(), loginRequest.getPassword());
        // 토큰 생성
        String token = jwtProvider.createToken(loginAdmin.getUsername(), true);

        return ResponseEntity.ok(
                new SuccessResponse()
                        .put("username", loginAdmin.getUsername())
                        .put("nickname", loginAdmin.getNickname())
                        .put("token", token));
    }


    @GetMapping("/check")
    public ResponseEntity<SuccessResponse> check(){

        return ResponseEntity.ok(new SuccessResponse());
    }
}
