package com.cgkim.myboard.controller;

import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.QuestionService;
import com.cgkim.myboard.util.FileHandler;
import com.cgkim.myboard.validator.FileSaveRequestValidator;
import com.cgkim.myboard.validator.GuestSaveRequestValidator;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.attach.FileSaveRequest;
import com.cgkim.myboard.vo.question.QuestionDetailResponse;
import com.cgkim.myboard.vo.question.QuestionListResponse;
import com.cgkim.myboard.vo.question.QuestionSaveRequest;
import com.cgkim.myboard.vo.question.QuestionSearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final FileHandler fileHandler;
    private final FileSaveRequestValidator fileSaveRequestValidator;
    private final GuestSaveRequestValidator guestSaveRequestValidator;

    /**
     * PropertyEditor, Validator 등록
     */
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {

        addPropertyEditors(webDataBinder);
        addValidators(webDataBinder);
    }

    /**
     * PropertyEditor 등록
     */
    private void addPropertyEditors(WebDataBinder webDataBinder) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
    }

    /**
     * Validator 등록
     */
    private void addValidators(WebDataBinder webDataBinder) {

        if (webDataBinder.getTarget() == null) {
            return;
        }

        final List<Validator> validatorList = List.of(
                fileSaveRequestValidator,
                guestSaveRequestValidator
        );

        for (Validator validator : validatorList) {
            if (validator.supports(webDataBinder.getTarget().getClass())) {
                webDataBinder.addValidators(validator);
            }
        }
    }

    /**
     * 질문 목록
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getList(QuestionSearchRequest questionSearchRequest){

        List<QuestionListResponse> questionList = questionService.getQuestionList(questionSearchRequest);
        int totalCounts = questionService.getTotalCounts(questionSearchRequest);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("itemList", questionList)
                        .put("totalCounts", totalCounts));
    }

    /**
     * 회원 질문 등록
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> write(Long memberId,
                                                 @Valid QuestionSaveRequest questionSaveRequest,
                                                 @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {

        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //첨부파일 생성 (C://upload)
        //TODO: 첨부파일 경로를 찢어야하나 찢어야겟지
        long id = questionService.write(memberId, questionSaveRequest, attachInsertList); //회원 질문 작성

        return ResponseEntity.created(URI.create("/questions/" + id)).body(new SuccessResponse());
    }

    /**
     * 질문 상세 보기 API
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> getBoardDetail(@PathVariable Long id) {

        QuestionDetailResponse questionDetail = questionService.viewDetail(id);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("itemDetail", questionDetail));
    }
}