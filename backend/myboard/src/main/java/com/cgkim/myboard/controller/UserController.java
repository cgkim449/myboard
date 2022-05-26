package com.cgkim.myboard.controller;

import com.cgkim.myboard.service.UserService;
import com.cgkim.myboard.vo.User.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    //TODO: 암호화, 유효성 검증
    @PostMapping
    public ResponseEntity<?> signUp(@RequestBody UserVo userVo) {
        userService.signUp(userVo);
        return ResponseEntity.ok().build();
    }
}
