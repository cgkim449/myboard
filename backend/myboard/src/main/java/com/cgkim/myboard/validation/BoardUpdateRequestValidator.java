package com.cgkim.myboard.validation;

import com.cgkim.myboard.vo.board.BoardUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 게시글 수정시 유효성 검증
 *
 */
@Component
public class BoardUpdateRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return BoardUpdateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BoardUpdateRequest boardUpdateRequest = (BoardUpdateRequest) target;

        String boardTitle = boardUpdateRequest.getBoardTitle();
        String boardContent = boardUpdateRequest.getBoardContent();

        if (boardTitle == null || !(4 <= boardTitle.length() && boardTitle.length() < 100)) {
            errors.rejectValue("boardTitle", "length", new Object[] {4, 100}, null);
        }

        if (boardContent == null || !(4 <= boardContent.length() && boardContent.length() < 2000)) {
            errors.rejectValue("boardContent", "length", new Object[] {4, 2000}, null);
        }
    }
}
