package com.cgkim.myboard.validation;

import com.cgkim.myboard.vo.member.GuestSaveRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 게시글 등록시 유효성 검증
 *
 */
@Slf4j
@Component
public class GuestSaveRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return GuestSaveRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("GuestVoValidtor 실행");
        GuestSaveRequest guestSaveRequest = (GuestSaveRequest) target;

        String guestNickname = guestSaveRequest.getGuestNickname();
        String guestPassword = guestSaveRequest.getGuestPassword();
        String guestPasswordConfirm = guestSaveRequest.getGuestPasswordConfirm();


        if (guestNickname == null || !(3 <= guestNickname.length() && guestNickname.length() < 5)) {
            errors.rejectValue("guestNickname", "length", new Object[] {3, 5}, null);
        }

        if (guestPassword == null || !isValid(guestPassword)) {
            errors.rejectValue("guestPassword", "pw", new Object[] {4, 16}, null);
        } else {
            if (!(guestPassword.equals(guestPasswordConfirm))) {
                errors.rejectValue("guestPasswordConfirm", "pwConfirm");
            }
        }

    }

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
