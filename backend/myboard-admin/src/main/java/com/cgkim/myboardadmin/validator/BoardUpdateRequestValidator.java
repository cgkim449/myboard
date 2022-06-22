package com.cgkim.myboardadmin.validator;


import com.cgkim.myboardadmin.vo.board.BoardUpdateRequest;
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

        String title = boardUpdateRequest.getTitle();
        String content = boardUpdateRequest.getContent();

        if (title == null || !(4 <= title.length() && title.length() < 100)) {
            errors.rejectValue("title", "length", new Object[] {4, 100}, null);
        }

        if (content == null || !(4 <= content.length() && content.length() < 2000)) {
            errors.rejectValue("content", "length", new Object[] {4, 2000}, null);
        }
    }
}
