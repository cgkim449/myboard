package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.argumentresolver.LoginUser;
import com.cgkim.myboardadmin.response.SuccessResponse;
import com.cgkim.myboardadmin.service.AdminService;
import com.cgkim.myboardadmin.service.AnswerService;
import com.cgkim.myboardadmin.service.impl.AnswerAttachServiceImpl;
import com.cgkim.myboardadmin.util.FileHandler;
import com.cgkim.myboardadmin.vo.answer.AnswerDetailResponse;
import com.cgkim.myboardadmin.vo.answer.AnswerSaveRequest;
import com.cgkim.myboardadmin.vo.answer.AnswerUpdateRequest;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.attach.FileSaveRequest;
import com.cgkim.myboardadmin.vo.board.BoardUpdateRequest;
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


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerService answerService;
    private final AnswerAttachServiceImpl attachService;
    private final FileHandler fileHandler;

    @PostMapping
    public ResponseEntity<SuccessResponse> write(@LoginUser String username,
                                                 @Valid AnswerSaveRequest answerSaveRequest,
                                                 @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {

        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //첨부파일 생성 (C://upload)

        Long answerId = answerService.write(username, answerSaveRequest, attachInsertList);

        return ResponseEntity.created(URI.create("/answers/" + answerId)).body(new SuccessResponse());
    }

    @GetMapping("/{answerId}")
    public ResponseEntity<SuccessResponse> getDetail(@PathVariable Long answerId) {

        AnswerDetailResponse answerDetail = answerService.viewDetail(answerId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("answerDetail", answerDetail));
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<SuccessResponse> deleteComment(@PathVariable Long answerId) {

        List<AttachVo> attachDeleteList = attachService.getList(answerId); //첨부파일 삭제 리스트
        answerService.delete(answerId);
        fileHandler.deleteFiles(attachDeleteList); //첨부파일 삭제 (C://upload)

        return ResponseEntity.noContent().build();
    }

    /**
     * 답변 수정
     * TODO: 답변 수정시 썸네일 업데이트 해야함.
     */
    @PatchMapping("/{answerId}")
    public ResponseEntity<SuccessResponse> updateAnswer(@PathVariable Long answerId,
                                                        @Valid AnswerUpdateRequest answerUpdateRequest,
                                                        @Valid FileSaveRequest fileSaveRequest,
                                                        Long[] attachDeleteRequest
    ) throws IOException {

        List<AttachVo> attachDeleteList = attachService.getList(attachDeleteRequest); //첨부파일 삭제 리스트
        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());//첨부파일 삽입 리스트

        answerService.modify( //게시글 수정, 첨부파일 수정
                answerId,
                answerUpdateRequest.getContent(),
                answerUpdateRequest.getTitle(),
                attachInsertList,
                attachDeleteList
        );

        fileHandler.deleteFiles(attachDeleteList); //첨부파일 삭제 (C://upload)

        return ResponseEntity.ok(new SuccessResponse());
    }
}