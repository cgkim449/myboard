package com.cgkim.myboard.validation;

import com.cgkim.myboard.vo.board.BoardUpdateRequest;
import com.cgkim.myboard.vo.user.SignUpRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 회원가입시 유효성 검증
 *
 */
@Component
public class SignUpRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return SignUpRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpRequest signUpRequest = (SignUpRequest) target;

        String username = signUpRequest.getUsername();
        String password = signUpRequest.getPassword();
        String passwordConfirm = signUpRequest.getPasswordConfirm();
        String nickname = signUpRequest.getNickname();

        if (username == null /*TODO: 올바른 이메일 형식인지 검증*/) {
            errors.rejectValue("username", "required");
        }

        if (password == null || !isValid(password)) {
            errors.rejectValue("password", "pw", new Object[] {4, 16}, null);
        } else {
            if (!(password.equals(passwordConfirm))) {
                errors.rejectValue("passwordConfirm", "pwConfirm");
            }
        }

        if (nickname == null || !(3 <= nickname.length() && nickname.length() < 5)) {
            errors.rejectValue("nickname", "length", new Object[] {3, 5}, null);
        }
    }

    /**
     * 회원가입시 비밀번호 검증
     *
     * @param password
     * @return
     */
    //TODO: 게시글 비밀번호 검증이랑 코드 중복됨
    private boolean isValid(String password) {
        if (!(4 <= password.length() && password.length() < 16)) {
            return false;
        }
        int alphabet = 0;
        int number = 0;
        int specialSymbol = 0;

        for (char c : password.toCharArray()) {
            if (!('!' <= c && c <= '~')) {
                return false;
            } else if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z')) {
                alphabet++;
            } else if ('0' <= c && c <= '9') {
                number++;
            } else {
                specialSymbol++;
            }
        }

        return alphabet != 0 && number != 0 && specialSymbol != 0;
    }
}
