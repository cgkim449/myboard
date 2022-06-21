package com.cgkim.myboard.controller;

import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.NoticeService;
import com.cgkim.myboard.vo.faq.FAQListResponse;
import com.cgkim.myboard.vo.notice.NoticeDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notices")
public class NoticeController {

    //TODO: 공지 첨부 이미지 썸네일?
    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<SuccessResponse> getDetail(){

        NoticeDetailResponse noticeDetail = noticeService.getNoticeDetail();

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("noticeDetail", noticeDetail));
    }
}