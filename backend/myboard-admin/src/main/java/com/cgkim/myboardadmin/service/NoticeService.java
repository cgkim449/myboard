package com.cgkim.myboardadmin.service;

import com.cgkim.myboardadmin.dao.AdminDao;
import com.cgkim.myboardadmin.dao.NoticeAttachDao;
import com.cgkim.myboardadmin.dao.NoticeDao;
import com.cgkim.myboardadmin.exception.NoticeInsertFailedException;
import com.cgkim.myboardadmin.exception.NoticeNotFoundException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.util.AttachURIProvider;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.board.BoardListResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeDetailResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeListResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeSaveRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeSearchRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeUpdateRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 공지사항 Service
 */
@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeDao noticeDao;

    private final AdminDao adminDao;

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
     * 검색조건에 해당하는 공지 총 갯수
     *
     * @param noticeSearchRequest
     * @return int
     */
    public int getTotalCount(NoticeSearchRequest noticeSearchRequest) {

        return noticeDao.selectCount(noticeSearchRequest);
    }

    /**
     * 공지 상세 조회
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
     * 가장 최근 공지 조회
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

        noticeDetailResponse.setAttachList(noticeAttachList); //첨부파일 리스트

        return noticeDetailResponse;
    }

    /**
     * 공지 작성
     *
     * @param username
     * @param noticeSaveRequest
     * @param attachInsertList
     * @return Long
     */
    @Transactional(rollbackFor = Exception.class)
    public Long write(String username, NoticeSaveRequest noticeSaveRequest, List<AttachVo> attachInsertList) {

        NoticeVo noticeVo = NoticeVo.builder()
                .title(noticeSaveRequest.getTitle())
                .content(noticeSaveRequest.getContent())
                .build();

        try {

            Long adminId = adminDao.selectAdminIdByUsername(username);
            noticeVo.setAdminId(adminId);
            noticeDao.insertNotice(noticeVo);

            Long noticeId = noticeVo.getNoticeId();

            if (isNotEmpty(attachInsertList)) {

                insertAttaches(attachInsertList, noticeId);
                updateHasAttach(noticeId);
                updateThumbnailUri(attachInsertList, noticeId);
            }

            return noticeId;

        } catch (Exception e) {

            e.printStackTrace();
            throw new NoticeInsertFailedException(attachInsertList, ErrorCode.NOTICE_INSERT_FAILED);
        }
    }

    private boolean isNotEmpty(List<AttachVo> attachList) {
        return attachList != null && !attachList.isEmpty();
    }

    private void updateThumbnailUri(List<AttachVo> attachList, Long noticeId) {

        for (AttachVo attach : attachList) {

            if (attach.isImage()) {

                String thumbnailUri = attachURIProvider.createThumbnailURIForDB(attach);
                noticeDao.updateThumbnailUri(Map.of("noticeId", noticeId, "thumbnailUri", thumbnailUri));

                return;
            }
        }

        noticeDao.updateThumbnailUri(Map.of("noticeId", noticeId, "thumbnailUri", ""));
    }

    private void insertAttaches(List<AttachVo> attachInsertList, Long noticeId) {

        for (AttachVo attach : attachInsertList) {

            attach.setNoticeId(noticeId);
            noticeAttachDao.insert(attach);
        }
    }

    private void deleteAttaches(List<AttachVo> attachDeleteList) {

        for (AttachVo attachVo : attachDeleteList) {

            noticeAttachDao.delete(attachVo.getAttachId());
        }
    }

    private void updateHasAttach(Long noticeId) {

        int attachCount = noticeAttachDao.selectCountByNoticeId(noticeId);
        boolean hasAttach = attachCount > 0;

        noticeDao.updateHasAttach(
                Map.of(
                        "noticeId", noticeId,
                        "hasAttach", hasAttach
                )
        );
    }

    /**
     * 공지사항 수정
     *
     * @param noticeId
     * @param noticeUpdateRequest
     * @param attachInsertList
     * @param attachDeleteList
     */
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long noticeId,
                       NoticeUpdateRequest noticeUpdateRequest,
                       List<AttachVo> attachInsertList,
                       List<AttachVo> attachDeleteList
    ) {

        if (isNotEmpty(attachDeleteList)) {

            deleteAttaches(attachDeleteList);
        }

        if (isNotEmpty(attachInsertList)) {

            insertAttaches(attachInsertList, noticeId);
        }

        noticeUpdateRequest.setNoticeId(noticeId);

        noticeDao.update(noticeUpdateRequest);
        updateHasAttach(noticeId);
        updateThumbnailUri(noticeAttachDao.selectList(noticeId), noticeId);
    }

    /**
     * 공지사항 삭제
     *
     * @param noticeId
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long noticeId) {

        noticeAttachDao.deleteByNoticeId(noticeId);
        noticeDao.delete(noticeId);
    }
}
