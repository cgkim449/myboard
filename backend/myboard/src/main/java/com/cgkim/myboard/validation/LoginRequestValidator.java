package com.cgkim.myboard.validation;

import com.cgkim.myboard.vo.member.LoginRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class LoginRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return LoginRequest.class.isAssignableFrom(clazz);
    }

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
