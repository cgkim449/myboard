package com.cgkim.myboard.controller;

import com.cgkim.myboard.config.jwt.JwtProvider;
import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.UserService;
import com.cgkim.myboard.validation.LoginRequestValidator;
import com.cgkim.myboard.validation.SignUpRequestValidator;
import com.cgkim.myboard.vo.user.LoginRequest;
import com.cgkim.myboard.vo.user.SignUpRequest;
import com.cgkim.myboard.vo.user.UserVo;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final SignUpRequestValidator signUpRequestValidator;
    private final LoginRequestValidator loginRequestValidator;

    private final JwtProvider jwtProvider;

    /**
     * Validator 등록
     *
     * @param webDataBinder
     */
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        addValidators(webDataBinder);
    }

    /**
     * Validator 등록
     *
     * @param webDataBinder
     */
    private void addValidators(WebDataBinder webDataBinder) {
        if (webDataBinder.getTarget() == null) {
            return;
        }

        final List<Validator> validatorList = List.of(
                signUpRequestValidator,
                loginRequestValidator
        );

        for (Validator validator : validatorList) {
            if (validator.supports(webDataBinder.getTarget().getClass())) {
                webDataBinder.addValidators(validator);
            }
        }
    }

    /**
     * 회원가입
     *
     * @param signUpRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return ResponseEntity.ok()
                .body(new SuccessResponse()
                        .put("signUpResult", userService.signUp(signUpRequest)));
    }

    /**
     * 로그인
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        // 로그인
        UserVo loginUser = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        // 토큰 생성
        String token = jwtProvider.createToken(loginUser.getUsername());

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(new SuccessResponse()
                        .put("userId", loginUser.getUserId())
                        .put("username", loginUser.getUsername())
                        .put("nickname", loginUser.getNickname()));
    }
}
