package com.cgkim.myboard.validator;


import com.cgkim.myboard.vo.notice.NoticeUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 공지 수정시 유효성 검증
 *
 */
@Component
public class NoticeUpdateRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return NoticeUpdateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NoticeUpdateRequest noticeUpdateRequest = (NoticeUpdateRequest) target;

        String title = noticeUpdateRequest.getTitle();
        String content = noticeUpdateRequest.getContent();

        if (title == null || !(4 <= title.length() && title.length() < 100)) {
            errors.rejectValue("title", "length", new Object[] {4, 100}, null);
        }

        if (content == null || !(4 <= content.length() && content.length() < 2000)) {
            errors.rejectValue("content", "length", new Object[] {4, 2000}, null);
        }
    }
}
