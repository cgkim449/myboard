package com.cgkim.myboard.controller;

import com.cgkim.myboard.argumentresolver.LoginUser;
import com.cgkim.myboard.exception.MemberNotFoundException;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.exception.LoginRequiredException;
import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.MemberService;
import com.cgkim.myboard.service.QuestionAttachService;
import com.cgkim.myboard.service.QuestionService;
import com.cgkim.myboard.util.FileHandler;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.common.FileSaveRequest;
import com.cgkim.myboard.vo.question.QuestionDetailResponse;
import com.cgkim.myboard.vo.question.QuestionListResponse;
import com.cgkim.myboard.vo.question.QuestionSaveRequest;
import com.cgkim.myboard.vo.question.QuestionSearchRequest;
import com.cgkim.myboard.vo.question.QuestionUpdateRequest;
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
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    private final MemberService memberService;

    private final QuestionAttachService attachService;

    private final FileHandler fileHandler;

    /**
     * 질문 목록 조회
     *
     * @param questionSearchRequest
     * @return ResponseEntity<SuccessResponse>
     */
    //TODO: 비공개 글 검색 안되게
    @GetMapping
    public ResponseEntity<SuccessResponse> getList(QuestionSearchRequest questionSearchRequest) {

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

        QuestionDetailResponse questionDetail = questionService.viewQuestionDetail(questionId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("questionDetail", questionDetail));
    }

    /**
     * 질문 작성(회원만 가능)
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

        if (username == null) {
            throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
        }

        Long memberId = memberService.getMemberIdBy(username);

        if (memberId == null) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //첨부파일 생성 (C://upload)
        Long questionId = questionService.write(memberId, questionSaveRequest, attachInsertList); //회원 질문 작성

        return ResponseEntity.created(URI.create("/questions/" + questionId)).body(new SuccessResponse());
    }

    /**
     * 질문 삭제
     *
     * @param username
     * @param questionId
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @DeleteMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> delete(@PathVariable Long questionId,
                                                  @LoginUser String username
    ) throws NoSuchAlgorithmException {

        questionService.checkOwner(questionId, username); //소유권 인증
        questionService.checkHasAnswer(questionId); //답변 유무 검증

        List<AttachVo> attachDeleteList = attachService.getList(questionId); //첨부파일 삭제 리스트
        questionService.delete(questionId); //게시물 삭제
        fileHandler.deleteFiles(attachDeleteList); //첨부파일 삭제 (C://upload)

        return ResponseEntity.noContent().build();
    }

    /**
     * 질문 수정
     *
     * @param username
     * @param questionId
     * @param questionUpdateRequest
     * @param fileSaveRequest
     * @param attachDeleteRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException
     */
    @PatchMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> updateQuestion(@PathVariable Long questionId,
                                                          @LoginUser String username,
                                                          @Valid QuestionUpdateRequest questionUpdateRequest,
                                                          @Valid FileSaveRequest fileSaveRequest,
                                                          Long[] attachDeleteRequest
    ) throws IOException {

        questionService.checkOwner(questionId, username);

        List<AttachVo> attachDeleteList = attachService.getList(attachDeleteRequest);
        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());

        questionService.modify(questionId, questionUpdateRequest, attachInsertList, attachDeleteList);

        fileHandler.deleteFiles(attachDeleteList);

        return ResponseEntity.ok(new SuccessResponse());
    }
}