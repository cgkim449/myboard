package com.cgkim.myboard.validator;

import com.cgkim.myboard.vo.member.LoginRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 로그인 요청시 유효성 검증
 */
@Component
public class LoginRequestValidator implements Validator {

    /**
     * 검증 대상 확인
     *
     * @param clazz
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return LoginRequest.class.isAssignableFrom(clazz);
    }

    /**
     * 검증
     *
     * @param target
     * @param errors
     */
    @Override
    public void validate(Object target, Errors errors) {

        LoginRequest loginRequest = (LoginRequest) target;

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (username == null) {
            errors.rejectValue("username", "required");
        }

        if (password == null) {
            errors.rejectValue("password", "required");
        }
    }
}
