package com.cgkim.myboardadmin.service.impl;

import com.cgkim.myboardadmin.dao.NoticeDao;
import com.cgkim.myboardadmin.service.NoticeService;
import com.cgkim.myboardadmin.vo.notice.NoticeDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
