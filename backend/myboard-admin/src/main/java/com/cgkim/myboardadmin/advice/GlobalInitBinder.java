package com.cgkim.myboardadmin.advice;

import com.cgkim.myboardadmin.service.impl.NoticeAttachServiceImpl;
import com.cgkim.myboardadmin.validator.BoardSaveRequestValidator;
import com.cgkim.myboardadmin.validator.BoardUpdateRequestValidator;
import com.cgkim.myboardadmin.validator.CommentSaveRequestValidator;
import com.cgkim.myboardadmin.validator.FAQSaveRequestValidator;
import com.cgkim.myboardadmin.validator.FileSaveRequestValidator;
import com.cgkim.myboardadmin.validator.GuestSaveRequestValidator;
import com.cgkim.myboardadmin.validator.LoginRequestValidator;
import com.cgkim.myboardadmin.validator.NoticeSaveRequestValidator;
import com.cgkim.myboardadmin.validator.NoticeUpdateRequestValidator;
import com.cgkim.myboardadmin.validator.QuestionSaveRequestValidator;
import com.cgkim.myboardadmin.validator.QuestionUpdateRequestValidator;
import com.cgkim.myboardadmin.validator.SignUpRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 전역 Validator, PropertyEditor 등록
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalInitBinder {
    private final BoardSaveRequestValidator boardSaveRequestValidator;
    private final BoardUpdateRequestValidator boardUpdateRequestValidator;
    private final CommentSaveRequestValidator commentSaveRequestValidator;
    private final QuestionSaveRequestValidator questionSaveRequestValidator;
    private final QuestionUpdateRequestValidator questionUpdateRequestValidator;
    private final FAQSaveRequestValidator faqSaveRequestValidator;
    private final NoticeSaveRequestValidator noticeSaveRequestValidator;
    private final NoticeUpdateRequestValidator noticeUpdateRequestValidator;
    private final FileSaveRequestValidator fileSaveRequestValidator;
    private final GuestSaveRequestValidator guestSaveRequestValidator;
    private final SignUpRequestValidator signUpRequestValidator;
    private final LoginRequestValidator loginRequestValidator;

    /**
     * Validator, PropertyEditor 등록
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        addPropertyEditors(binder);
        addValidators(binder);
    }

    /**
     * PropertyEditor 등록
     * @param binder
     */
    private void addPropertyEditors(WebDataBinder binder) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
    }

    /**
     * Validator 등록
     * @param binder
     */
    private void addValidators(WebDataBinder binder) {

        if (binder.getTarget() == null) {
            return;
        }

        final List<Validator> validatorList = List.of(
                boardSaveRequestValidator,
                boardUpdateRequestValidator,
                commentSaveRequestValidator,
                questionSaveRequestValidator,
                questionUpdateRequestValidator,
                faqSaveRequestValidator,
                noticeSaveRequestValidator,
                noticeUpdateRequestValidator,
                fileSaveRequestValidator,
                guestSaveRequestValidator,
                signUpRequestValidator,
                loginRequestValidator
        );

        for (Validator validator : validatorList) {
            if (validator.supports(binder.getTarget().getClass())) {
                binder.addValidators(validator);
            }
        }
    }
}
