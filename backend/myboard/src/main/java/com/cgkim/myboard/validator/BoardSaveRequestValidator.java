package com.cgkim.myboard.validator;

import com.cgkim.myboard.vo.board.BoardSaveRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 게시글 등록시 유효성 검증
 */
@Component
public class BoardSaveRequestValidator implements Validator {

    /**
     * 검증 대상 확인
     *
     * @param clazz
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return BoardSaveRequest.class.isAssignableFrom(clazz);
    }

    /**
     * 검증
     *
     * @param target the object that is to be validated
     * @param errors contextual state about the validation process
     */
    @Override
    public void validate(Object target, Errors errors) {

        BoardSaveRequest boardSaveRequest = (BoardSaveRequest) target;

        Integer categoryId = boardSaveRequest.getCategoryId();
        String title = boardSaveRequest.getTitle();
        String content = boardSaveRequest.getContent();

        if (categoryId == null || categoryId == 0) {
            errors.rejectValue("categoryId", "required");
        }

        if (title == null || !(4 <= title.length() && title.length() < 100)) {
            errors.rejectValue("title", "length", new Object[] {4, 100}, null);
        }

        if (content == null || !(4 <= content.length() && content.length() < 2000)) {
            errors.rejectValue("content", "length", new Object[] {4, 2000}, null);
        }
    }
}
