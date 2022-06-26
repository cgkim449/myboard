package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.argumentresolver.LoginUser;
import com.cgkim.myboardadmin.response.SuccessResponse;
import com.cgkim.myboardadmin.service.AnswerService;
import com.cgkim.myboardadmin.service.QuestionAttachService;
import com.cgkim.myboardadmin.service.QuestionService;
import com.cgkim.myboardadmin.util.FileHandler;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.common.FileSaveRequest;
import com.cgkim.myboardadmin.vo.question.QuestionDetailResponse;
import com.cgkim.myboardadmin.vo.question.QuestionListResponse;
import com.cgkim.myboardadmin.vo.question.QuestionSaveRequest;
import com.cgkim.myboardadmin.vo.question.QuestionSearchRequest;
import com.cgkim.myboardadmin.vo.question.QuestionUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Q&A 질문 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/questions")
public class QuestionController {

    private final QuestionService questionService;

    private final AnswerService answerService;

    private final QuestionAttachService attachService;

    private final FileHandler fileHandler;

    /**
     * 질문 목록 조회
     *
     * @param questionSearchRequest
     * @return ResponseEntity<SuccessResponse>
     */
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
     *
     * @param questionId
     * @return ResponseEntity<SuccessResponse>
     */
    @GetMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> getDetail(@PathVariable Long questionId) {

        QuestionDetailResponse questionDetail = questionService.viewDetail(questionId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("questionDetail", questionDetail));
    }

    /**
     * 질문 작성
     *
     * @param username
     * @param questionSaveRequest
     * @param fileSaveRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> write(@LoginUser String username,
                                                 @Valid QuestionSaveRequest questionSaveRequest,
                                                 @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {

        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());
        long questionId = questionService.write(username, questionSaveRequest, attachInsertList);

        return ResponseEntity.created(URI.create("/admin/questions/" + questionId)).body(new SuccessResponse());
    }

    /**
     * 질문 삭제
     *
     * @param questionId
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @DeleteMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> delete(@PathVariable Long questionId) throws NoSuchAlgorithmException {

        List<AttachVo> attachDeleteList = attachService.getList(questionId);

        answerService.deleteByQuestionId(questionId);
        questionService.delete(questionId);
        fileHandler.deleteFiles(attachDeleteList);

        return ResponseEntity.noContent().build();
    }

    /**
     * 질문 수정
     *
     * @param questionId
     * @param questionUpdateRequest
     * @param fileSaveRequest
     * @param attachDeleteRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException
     */
    @PatchMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> updateQuestion(@PathVariable Long questionId,
                                                          @Valid QuestionUpdateRequest questionUpdateRequest,
                                                          @Valid FileSaveRequest fileSaveRequest,
                                                          Long[] attachDeleteRequest
    ) throws IOException {

        List<AttachVo> attachDeleteList = attachService.getList(attachDeleteRequest);
        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());

        questionService.modify(questionId, questionUpdateRequest, attachInsertList, attachDeleteList);

        fileHandler.deleteFiles(attachDeleteList); //첨부파일 삭제 (C://upload)

        return ResponseEntity.ok(new SuccessResponse());
    }
}