package com.cgkim.myboard.controller;

import com.cgkim.myboard.argumentresolver.LoginUser;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.exception.LoginRequiredException;
import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.AnswerService;
import com.cgkim.myboard.service.QuestionService;
import com.cgkim.myboard.service.impl.QuestionAttachServiceImpl;
import com.cgkim.myboard.util.FileHandler;
import com.cgkim.myboard.validator.FileSaveRequestValidator;
import com.cgkim.myboard.validator.GuestSaveRequestValidator;
import com.cgkim.myboard.validator.QuestionSaveRequestValidator;
import com.cgkim.myboard.validator.QuestionUpdateRequestValidator;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.attach.FileSaveRequest;
import com.cgkim.myboard.vo.question.QuestionDetailResponse;
import com.cgkim.myboard.vo.question.QuestionListResponse;
import com.cgkim.myboard.vo.question.QuestionSaveRequest;
import com.cgkim.myboard.vo.question.QuestionSearchRequest;
import com.cgkim.myboard.vo.question.QuestionUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Q&A 질문 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionAttachServiceImpl attachService;
    private final FileHandler fileHandler;

    /**
     * 질문 목록 조회
     * @param questionSearchRequest
     * @return
     */
    //TODO: 비공개 글 검색 안되게
    @GetMapping
    public ResponseEntity<SuccessResponse> getList(QuestionSearchRequest questionSearchRequest){

        List<QuestionListResponse> questionList = questionService.getQuestionList(questionSearchRequest);
        int questionTotalCount = questionService.getTotalCount(questionSearchRequest);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("questionList", questionList)
                        .put("questionTotalCount", questionTotalCount));
    }

    /**
     * 질문 상세 조회
     * @param questionId
     * @return
     */
    @GetMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> getDetail(@PathVariable Long questionId) {

        QuestionDetailResponse questionDetail = questionService.viewDetail(questionId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("questionDetail", questionDetail));
    }

    /**
     * 질문 작성(회원만 가능)
     * @param username
     * @param questionSaveRequest
     * @param fileSaveRequest
     * @return
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> write(@LoginUser String username,
                                                 @Valid QuestionSaveRequest questionSaveRequest,
                                                 @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {

        if(username == null) {
            throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
        }

        //TODO: 첨부파일 경로 찢기
        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //첨부파일 생성 (C://upload)
        long questionId = questionService.write(username, questionSaveRequest, attachInsertList); //회원 질문 작성

        return ResponseEntity.created(URI.create("/questions/" + questionId)).body(new SuccessResponse());
    }

    /**
     * 질문 삭제
     * @param username
     * @param questionId
     * @return
     * @throws NoSuchAlgorithmException
     */
    @DeleteMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> delete(@LoginUser String username,
                                                  @PathVariable Long questionId
    ) throws NoSuchAlgorithmException {

        //소유권 인증
        questionService.checkOwner(questionId, username);

        //답변이 달린 질문은 수정, 삭제 불가
        questionService.checkHasAnswer(questionId);

        List<AttachVo> attachDeleteList = attachService.getList(questionId); //첨부파일 삭제 리스트
        questionService.delete(questionId); //게시물 삭제
        fileHandler.deleteFiles(attachDeleteList); //첨부파일 삭제 (C://upload)

        return ResponseEntity.noContent().build();
    }

    /**
     * 질문 수정
     * @param username
     * @param questionId
     * @param questionUpdateRequest
     * @param fileSaveRequest
     * @param attachDeleteRequest
     * @return
     * @throws IOException
     */
    @PatchMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> updateQuestion(@LoginUser String username,
                                                          @PathVariable Long questionId,
                                                          @Valid QuestionUpdateRequest questionUpdateRequest,
                                                          @Valid FileSaveRequest fileSaveRequest,
                                                          Long[] attachDeleteRequest
    ) throws IOException {
        //소유권 인증.
        questionService.checkOwner(questionId, username);

        List<AttachVo> attachDeleteList = attachService.getList(attachDeleteRequest); //첨부파일 삭제 리스트
        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());//첨부파일 삽입 리스트

        //게시글 수정, 첨부파일 수정
        questionService.modify(
                questionId,
                questionUpdateRequest.getContent(),
                questionUpdateRequest.getTitle(),
                questionUpdateRequest.getIsSecret(),
                attachInsertList,
                attachDeleteList
        );

        fileHandler.deleteFiles(attachDeleteList); //첨부파일 삭제 (C://upload)

        return ResponseEntity.ok(new SuccessResponse());
    }
}