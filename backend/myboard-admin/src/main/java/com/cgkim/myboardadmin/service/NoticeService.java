package com.cgkim.myboardadmin.service;


import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.notice.NoticeDetailResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeListResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeSaveRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeSearchRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NoticeService {
    List<NoticeListResponse> getNoticeList(NoticeSearchRequest noticeSearchRequest);

    int getTotalCount(NoticeSearchRequest noticeSearchRequest);

    NoticeDetailResponse viewNoticeDetail(Long noticeId);

    @Transactional(rollbackFor = Exception.class)
    long write(String username, NoticeSaveRequest noticeSaveRequest, List<AttachVo> attachInsertList);


    @Transactional(rollbackFor = Exception.class)
    void delete(Long noticeId);

    @Transactional(rollbackFor = Exception.class)
    void modify(Long noticeId,
                String noticeContent,
                String noticeTitle,
                List<AttachVo> attachInsertList,
                List<AttachVo> attachDeleteList);

    NoticeDetailResponse getLatestNoticeDetail();
}
