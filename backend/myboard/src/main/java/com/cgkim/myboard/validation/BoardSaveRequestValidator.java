package com.cgkim.myboard.validation;

import com.cgkim.myboard.vo.board.BoardSaveRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 게시글 등록시 유효성 검증
 *
 */
@Component
public class BoardSaveRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return BoardSaveRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BoardSaveRequest boardSaveRequest = (BoardSaveRequest) target;

        Integer categoryId = boardSaveRequest.getCategoryId();
        String boardTitle = boardSaveRequest.getBoardTitle();
        String boardContent = boardSaveRequest.getBoardContent();
        String guestName = boardSaveRequest.getGuestName();
        String guestPassword = boardSaveRequest.getGuestPassword();
        String guestPasswordConfirm = boardSaveRequest.getGuestPasswordConfirm();

        if (categoryId == null || categoryId == 0) {
            errors.rejectValue("categoryId", "required");
        }

        if (guestName == null || !(3 <= guestName.length() && guestName.length() < 5)) {
            errors.rejectValue("guestName", "length", new Object[] {3, 5}, null);
        }

        if (guestPassword == null || !isValid(guestPassword)) {
            errors.rejectValue("guestPassword", "pw", new Object[] {4, 16}, null);
        } else {
            if (!(guestPassword.equals(guestPasswordConfirm))) {
                errors.rejectValue("guestPasswordConfirm", "pwConfirm");
            }
        }

        if (boardTitle == null || !(4 <= boardTitle.length() && boardTitle.length() < 100)) {
            errors.rejectValue("boardTitle", "length", new Object[] {4, 100}, null);
        }

        if (boardContent == null || !(4 <= boardContent.length() && boardContent.length() < 2000)) {
            errors.rejectValue("boardContent", "length", new Object[] {4, 2000}, null);
        }
    }

    /**
     * 게시글 비밀번호 유효성 검증
     *
     * @param boardPw
     * @return
     */
    private boolean isValid(String boardPw) {
        if (!(4 <= boardPw.length() && boardPw.length() < 16)) {
            return false;
        }
        int alphabet = 0;
        int number = 0;
        int specialSymbol = 0;

        for (char c : boardPw.toCharArray()) {
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
