package com.cgkim.myboard.advice;

import com.cgkim.myboard.validator.BoardSaveRequestValidator;
import com.cgkim.myboard.validator.BoardUpdateRequestValidator;
import com.cgkim.myboard.validator.CommentSaveRequestValidator;
import com.cgkim.myboard.validator.FileSaveRequestValidator;
import com.cgkim.myboard.validator.GuestSaveRequestValidator;
import com.cgkim.myboard.validator.LoginRequestValidator;
import com.cgkim.myboard.validator.QuestionSaveRequestValidator;
import com.cgkim.myboard.validator.QuestionUpdateRequestValidator;
import com.cgkim.myboard.validator.SignUpRequestValidator;
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
