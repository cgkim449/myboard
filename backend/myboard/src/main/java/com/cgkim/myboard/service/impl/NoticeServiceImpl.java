package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.FAQDao;
import com.cgkim.myboard.dao.NoticeDao;
import com.cgkim.myboard.service.FAQService;
import com.cgkim.myboard.service.NoticeService;
import com.cgkim.myboard.vo.faq.FAQListResponse;
import com.cgkim.myboard.vo.notice.NoticeDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeDao noticeDao;

    @Override
    public NoticeDetailResponse getNoticeDetail() {
        NoticeDetailResponse noticeDetail = noticeDao.selectLatestOne();
        return noticeDetail;
    }
}
