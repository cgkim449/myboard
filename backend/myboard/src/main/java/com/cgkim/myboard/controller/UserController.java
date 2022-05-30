package com.cgkim.myboard.controller;

import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.UserService;
import com.cgkim.myboard.validation.BoardUpdateRequestValidator;
import com.cgkim.myboard.validation.SignUpRequestValidator;
import com.cgkim.myboard.vo.user.SignUpRequest;
import com.cgkim.myboard.vo.user.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final SignUpRequestValidator signUpRequestValidator;

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
                signUpRequestValidator
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
    public ResponseEntity<SuccessResponse> signUp(@Valid SignUpRequest signUpRequest) {
        return ResponseEntity.ok()
                .body(new SuccessResponse()
                        .put("signUpResult", userService.signUp(signUpRequest)));
    }
}
