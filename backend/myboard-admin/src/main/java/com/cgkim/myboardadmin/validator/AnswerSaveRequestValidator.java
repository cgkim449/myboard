package com.cgkim.myboardadmin.validator;


import com.cgkim.myboardadmin.vo.answer.AnswerSaveRequest;
import com.cgkim.myboardadmin.vo.question.QuestionSaveRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 질문 등록시 유효성 검증
 *
 */
@Component
public class AnswerSaveRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return AnswerSaveRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AnswerSaveRequest answerSaveRequest = (AnswerSaveRequest) target;

        String title = answerSaveRequest.getTitle();
        String content = answerSaveRequest.getContent();

        if (title == null || !(4 <= title.length() && title.length() < 100)) {
            errors.rejectValue("title", "length", new Object[] {4, 100}, null);
        }

        if (content == null || !(4 <= content.length() && content.length() < 2000)) {
            errors.rejectValue("content", "length", new Object[] {4, 2000}, null);
        }
    }
}
