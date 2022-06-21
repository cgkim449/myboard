package com.cgkim.myboard.service;

import com.cgkim.myboard.vo.faq.FAQListResponse;
import com.cgkim.myboard.vo.notice.NoticeDetailResponse;

import java.util.List;

public interface NoticeService {
    NoticeDetailResponse getNoticeDetail();
}
