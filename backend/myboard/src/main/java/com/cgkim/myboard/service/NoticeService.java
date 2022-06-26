package com.cgkim.myboard.service;

import com.cgkim.myboard.dao.NoticeAttachDao;
import com.cgkim.myboard.dao.NoticeDao;
import com.cgkim.myboard.exception.NoticeNotFoundException;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.util.AttachURIProvider;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.notice.NoticeDetailResponse;
import com.cgkim.myboard.vo.notice.NoticeListResponse;
import com.cgkim.myboard.vo.notice.NoticeSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 공지사항 Service
 */
@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeDao noticeDao;

    private final NoticeAttachDao noticeAttachDao;

    private final AttachURIProvider attachURIProvider;

    /**
     * 공지사항 목록 조회
     *
     * @param noticeSearchRequest
     * @return List<NoticeListResponse>
     */
    public List<NoticeListResponse> getNoticeList(NoticeSearchRequest noticeSearchRequest) {

        List<NoticeListResponse> noticeList = noticeDao.selectList(noticeSearchRequest);

        setThumbnailURIsOf(noticeList);

        return noticeList;
    }

    private void setThumbnailURIsOf(List<NoticeListResponse> noticeList) {

        for (NoticeListResponse noticeListResponse : noticeList) {
            setThumbnailURIOf(noticeListResponse);
        }
    }

    public void setThumbnailURIOf(NoticeListResponse noticeListResponse) {

        String thumbnailURI = noticeListResponse.getThumbnailUri();

        if (thumbnailURI != null) {
            noticeListResponse.setThumbnailUri(attachURIProvider.getFullURIOf(thumbnailURI));
        }
    }

    /**
     * 검색조건에 해당하는 공지사항 총 갯수
     *
     * @param noticeSearchRequest
     * @return int
     */
    public int getTotalCount(NoticeSearchRequest noticeSearchRequest) {
        return noticeDao.selectCount(noticeSearchRequest);
    }

    /**
     * 공지사항 상세 조회
     *
     * @param noticeId
     * @return NoticeDetailResponse
     */
    public NoticeDetailResponse viewNoticeDetail(Long noticeId) {

        NoticeDetailResponse noticeDetailResponse = noticeDao.selectOne(noticeId);

        if (noticeDetailResponse == null) {
            throw new NoticeNotFoundException(ErrorCode.NOTICE_NOT_FOUND);
        }

        List<AttachVo> noticeAttachList = noticeAttachDao.selectList(noticeId);

        attachURIProvider.setImageURIsOf(noticeAttachList);

        noticeDetailResponse.setAttachList(noticeAttachList);

        return noticeDetailResponse;
    }

    /**
     * 가장 최근 공지 상세 조회
     *
     * @return NoticeDetailResponse
     */
    public NoticeDetailResponse getLatestNoticeDetail() {

        NoticeDetailResponse noticeDetailResponse = noticeDao.selectLatestOne();

        if (noticeDetailResponse == null) {
            throw new NoticeNotFoundException(ErrorCode.NOTICE_NOT_FOUND);
        }

        List<AttachVo> noticeAttachList = noticeAttachDao.selectList(noticeDetailResponse.getNoticeId());

        attachURIProvider.setImageURIsOf(noticeAttachList);

        noticeDetailResponse.setAttachList(noticeAttachList);

        return noticeDetailResponse;
    }
}
