package com.cgkim.myboard.validator;

import com.cgkim.myboard.vo.board.BoardUpdateRequest;
import com.cgkim.myboard.vo.question.QuestionUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 게시글 수정시 유효성 검증
 */
@Component
public class QuestionUpdateRequestValidator implements Validator {

    /**
     * 검증 대상 확인
     *
     * @param clazz
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return QuestionUpdateRequest.class.isAssignableFrom(clazz);
    }

    /**
     * 검증
     *
     * @param target
     * @param errors
     */
    @Override
    public void validate(Object target, Errors errors) {

        QuestionUpdateRequest questionUpdateRequest = (QuestionUpdateRequest) target;

        String title = questionUpdateRequest.getTitle();
        String content = questionUpdateRequest.getContent();
        Integer isSecret = questionUpdateRequest.getIsSecret();

        if (title == null || !(4 <= title.length() && title.length() < 100)) {
            errors.rejectValue("title", "length", new Object[]{4, 100}, null);
        }

        if (content == null || !(4 <= content.length() && content.length() < 2000)) {
            errors.rejectValue("content", "length", new Object[]{4, 2000}, null);
        }

        if (isSecret == null) {
            errors.rejectValue("isSecret", "required");
        }
    }
}
