package com.cgkim.myboard.validator;

import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 댓글 등록시 유효성 검증
 */
@Component
public class CommentSaveRequestValidator implements Validator {

    /**
     * 검증 대상 확인
     * @param clazz
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return CommentSaveRequest.class.isAssignableFrom(clazz);
    }

    /**
     * 검증
     *
     * @param target
     * @param errors
     */
    @Override
    public void validate(Object target, Errors errors) {

        CommentSaveRequest commentSaveRequest = (CommentSaveRequest) target;

        String content = commentSaveRequest.getContent();

        if (content == null || !(1 <= content.length() && content.length() < 100)) {
            errors.rejectValue("content", "length", new Object[] {1, 100}, null);
        }
    }
}
