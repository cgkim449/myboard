package com.cgkim.myboard.validator;

import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 댓글 등록시 유효성 검증
 *
 */
@Component
public class CommentSaveRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CommentSaveRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CommentSaveRequest commentSaveRequest = (CommentSaveRequest) target;

        String commentContent = commentSaveRequest.getContent();

        if (commentContent == null || !(1 <= commentContent.length() && commentContent.length() < 100)) {
            errors.rejectValue("content", "length", new Object[] {1, 100}, null);
        }
    }
}
