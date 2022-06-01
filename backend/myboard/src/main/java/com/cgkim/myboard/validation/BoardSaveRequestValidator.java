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

        if (categoryId == null || categoryId == 0) {
            errors.rejectValue("categoryId", "required");
        }

        if (boardTitle == null || !(4 <= boardTitle.length() && boardTitle.length() < 100)) {
            errors.rejectValue("boardTitle", "length", new Object[] {4, 100}, null);
        }

        if (boardContent == null || !(4 <= boardContent.length() && boardContent.length() < 2000)) {
            errors.rejectValue("boardContent", "length", new Object[] {4, 2000}, null);
        }
    }
}
