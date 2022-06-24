package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.argumentresolver.LoginUser;
import com.cgkim.myboardadmin.response.SuccessResponse;
import com.cgkim.myboardadmin.service.NoticeService;
import com.cgkim.myboardadmin.service.impl.NoticeAttachServiceImpl;
import com.cgkim.myboardadmin.util.FileHandler;
import com.cgkim.myboardadmin.validator.FileSaveRequestValidator;
import com.cgkim.myboardadmin.validator.NoticeSaveRequestValidator;
import com.cgkim.myboardadmin.validator.NoticeUpdateRequestValidator;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.attach.FileSaveRequest;
import com.cgkim.myboardadmin.vo.board.BoardUpdateRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeDetailResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeListResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeSaveRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeSearchRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeUpdateRequest;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/notices")
public class NoticeController {

    //TODO: 공지 첨부 이미지 썸네일?
    private final NoticeService noticeService;
    private final NoticeAttachServiceImpl attachService;
    private final FileHandler fileHandler;

    /**
     * 공지 상세 조회
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<SuccessResponse> getDetail(@PathVariable Long noticeId) {

        NoticeDetailResponse noticeDetail = noticeService.viewNoticeDetail(noticeId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("noticeDetail", noticeDetail));
    }

    /**
     * 가장 최근 공지 조회
     */
    @GetMapping("/latest")
    public ResponseEntity<SuccessResponse> getLatestNoticeDetail() {

        NoticeDetailResponse noticeDetail = noticeService.getLatestNoticeDetail();

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("noticeDetail", noticeDetail));
    }

    /**
     * 공지 목록 조회
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getList(NoticeSearchRequest noticeSearchRequest){

        List<NoticeListResponse> noticeList = noticeService.getNoticeList(noticeSearchRequest);
        int noticeTotalCount = noticeService.getTotalCount(noticeSearchRequest);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("noticeList", noticeList)
                        .put("noticeTotalCount", noticeTotalCount));
    }

    /**
     * 공지 작성
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> writeBoard(@LoginUser String username,
                                                      @Valid NoticeSaveRequest noticeSaveRequest,
                                                      @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {

        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //첨부파일 생성 (C://upload)
        long noticeId = noticeService.write(username, noticeSaveRequest, attachInsertList); //글 작성

        return ResponseEntity.created(URI.create("/admin/notices/" + noticeId)).body(new SuccessResponse());
    }


    /**
     * 공지 삭제
     */
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<SuccessResponse> delete(@PathVariable Long noticeId) {

        List<AttachVo> attachDeleteList = attachService.getList(noticeId); //첨부파일 삭제 리스트
        noticeService.delete(noticeId); //게시물 삭제
        fileHandler.deleteFiles(attachDeleteList); //첨부파일 삭제 (C://upload)

        return ResponseEntity.noContent().build();
    }

    /**
     * 공지 수정
     */
    @PatchMapping("/{noticeId}")
    public ResponseEntity<SuccessResponse> update(@PathVariable Long noticeId,
                                                  @Valid NoticeUpdateRequest noticeUpdateRequest,
                                                  @Valid FileSaveRequest fileSaveRequest,
                                                  Long[] attachDeleteRequest
    ) throws IOException {

        List<AttachVo> attachDeleteList = attachService.getList(attachDeleteRequest); //첨부파일 삭제 리스트
        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());//첨부파일 삽입 리스트

        noticeService.modify( //공지 수정, 첨부파일 수정
                noticeId,
                noticeUpdateRequest.getContent(),
                noticeUpdateRequest.getTitle(),
                attachInsertList,
                attachDeleteList
        );

        fileHandler.deleteFiles(attachDeleteList); //첨부파일 삭제 (C://upload)

        return ResponseEntity.ok(new SuccessResponse());
    }
}