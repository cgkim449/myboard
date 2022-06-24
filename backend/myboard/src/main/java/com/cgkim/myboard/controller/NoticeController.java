package com.cgkim.myboard.controller;

import com.cgkim.myboard.argumentresolver.LoginUser;
import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.NoticeService;
import com.cgkim.myboard.service.impl.NoticeAttachServiceImpl;
import com.cgkim.myboard.util.FileHandler;
import com.cgkim.myboard.validator.FileSaveRequestValidator;
import com.cgkim.myboard.validator.NoticeSaveRequestValidator;
import com.cgkim.myboard.validator.NoticeUpdateRequestValidator;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.attach.FileSaveRequest;
import com.cgkim.myboard.vo.notice.NoticeDetailResponse;
import com.cgkim.myboard.vo.notice.NoticeListResponse;
import com.cgkim.myboard.vo.notice.NoticeSaveRequest;
import com.cgkim.myboard.vo.notice.NoticeSearchRequest;
import com.cgkim.myboard.vo.notice.NoticeUpdateRequest;
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

/**
 * 공지사항 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 공지사항 상세 조회
     * @param noticeId
     * @return
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<SuccessResponse> getDetail(@PathVariable Long noticeId) {

        NoticeDetailResponse noticeDetail = noticeService.viewNoticeDetail(noticeId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("noticeDetail", noticeDetail));
    }

    /**
     * 가장 최근 공지사항 조회
     * @return
     */
    @GetMapping("/latest")
    public ResponseEntity<SuccessResponse> getLatestNoticeDetail() {

        NoticeDetailResponse noticeDetail = noticeService.getLatestNoticeDetail();

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("noticeDetail", noticeDetail));
    }

    /**
     * 공지사항 목록 조회
     * @param noticeSearchRequest
     * @return
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
}