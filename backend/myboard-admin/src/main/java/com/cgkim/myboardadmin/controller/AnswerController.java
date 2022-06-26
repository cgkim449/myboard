package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.argumentresolver.LoginUser;
import com.cgkim.myboardadmin.response.SuccessResponse;
import com.cgkim.myboardadmin.service.AnswerAttachService;
import com.cgkim.myboardadmin.service.AnswerService;
import com.cgkim.myboardadmin.util.FileHandler;
import com.cgkim.myboardadmin.vo.answer.AnswerDetailResponse;
import com.cgkim.myboardadmin.vo.answer.AnswerSaveRequest;
import com.cgkim.myboardadmin.vo.answer.AnswerUpdateRequest;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.common.FileSaveRequest;
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
import java.util.List;

/**
 * Q&A 답변 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/answers")
public class AnswerController {

    private final AnswerService answerService;

    private final AnswerAttachService attachService;

    private final FileHandler fileHandler;


    /**
     * 답변 작성
     *
     * @param username
     * @param answerSaveRequest
     * @param fileSaveRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> write(@LoginUser String username,
                                                 @Valid AnswerSaveRequest answerSaveRequest,
                                                 @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {

        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());

        Long answerId = answerService.write(username, answerSaveRequest, attachInsertList);

        return ResponseEntity.created(URI.create("/answers/" + answerId)).body(new SuccessResponse());
    }

    /**
     * 답변 상세 조회
     *
     * @param answerId
     * @return ResponseEntity<SuccessResponse>
     */
    @GetMapping("/{answerId}")
    public ResponseEntity<SuccessResponse> getDetail(@PathVariable Long answerId) {

        AnswerDetailResponse answerDetail = answerService.viewAnswerDetail(answerId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("answerDetail", answerDetail));
    }

    /**
     * 답변 삭제
     *
     * @param answerId
     * @return ResponseEntity<SuccessResponse>
     */
    @DeleteMapping("/{answerId}")
    public ResponseEntity<SuccessResponse> deleteComment(@PathVariable Long answerId) {

        List<AttachVo> attachDeleteList = attachService.getList(answerId); //첨부파일 삭제 리스트
        answerService.delete(answerId);
        fileHandler.deleteFiles(attachDeleteList); //첨부파일 삭제 (C://upload)

        return ResponseEntity.noContent().build();
    }

    /**
     * 답변 수정
     *
     * @param answerId
     * @param answerUpdateRequest
     * @param fileSaveRequest
     * @param attachDeleteRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException
     */
    @PatchMapping("/{answerId}")
    public ResponseEntity<SuccessResponse> updateAnswer(@PathVariable Long answerId,
                                                        @Valid AnswerUpdateRequest answerUpdateRequest,
                                                        @Valid FileSaveRequest fileSaveRequest,
                                                        Long[] attachDeleteRequest
    ) throws IOException {

        List<AttachVo> attachDeleteList = attachService.getList(attachDeleteRequest);
        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());

        answerService.modify(answerId, answerUpdateRequest, attachInsertList, attachDeleteList);

        fileHandler.deleteFiles(attachDeleteList); //첨부파일 삭제 (C://upload)

        return ResponseEntity.ok(new SuccessResponse());
    }
}