package com.cgkim.myboardadmin.validator;


import com.cgkim.myboardadmin.vo.board.BoardSaveRequest;
import com.cgkim.myboardadmin.vo.faq.FAQSaveRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * FAQ 등록시 유효성 검증
 *
 */
@Component
public class FAQSaveRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return FAQSaveRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FAQSaveRequest faqSaveRequest = (FAQSaveRequest) target;

        Integer categoryId = faqSaveRequest.getCategoryId();
        String title = faqSaveRequest.getTitle();
        String content = faqSaveRequest.getContent();

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
