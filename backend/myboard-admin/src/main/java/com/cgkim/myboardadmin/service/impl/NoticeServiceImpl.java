package com.cgkim.myboardadmin.service.impl;

import com.cgkim.myboardadmin.dao.AdminDao;
import com.cgkim.myboardadmin.dao.NoticeAttachDao;
import com.cgkim.myboardadmin.dao.NoticeDao;
import com.cgkim.myboardadmin.exception.NoticeInsertFailedException;
import com.cgkim.myboardadmin.exception.NoticeNotFoundException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.service.NoticeService;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.notice.NoticeDetailResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeListResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeSaveRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeSearchRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeDao noticeDao;
    private final AdminDao adminDao;
    private final NoticeAttachDao noticeAttachDao;

    @Value("${host.url}")
    private String hostUrl;

    /**
     * 검색조건에 해당하는 공지 리스트
     */
    @Override
    public List<NoticeListResponse> getNoticeList(NoticeSearchRequest noticeSearchRequest) {
        List<NoticeListResponse> noticeList = noticeDao.selectList(noticeSearchRequest);
        //TODO: 리팩토링
        for (NoticeListResponse noticeListResponse : noticeList) {
            String thumbnailUri = noticeListResponse.getThumbnailUri();
            if(thumbnailUri != null) {
                noticeListResponse.setThumbnailUri(hostUrl + "upload" + File.separator + thumbnailUri);
            }
        }

        return noticeList;
    }

    /**
     * 검색조건에 해당하는 공지 총 갯수
     */
    @Override
    public int getTotalCount(NoticeSearchRequest noticeSearchRequest) {
        return noticeDao.selectCount(noticeSearchRequest);
    }

    /**
     * 게시물 상세 보기
     */
    @Override
    public NoticeDetailResponse viewNoticeDetail(Long noticeId) {
        NoticeDetailResponse noticeDetailResponse = noticeDao.selectOne(noticeId);

        if(noticeDetailResponse == null) {
            throw new NoticeNotFoundException(ErrorCode.NOTICE_NOT_FOUND);
        }

        List<AttachVo> attachVoList = noticeAttachDao.selectList(noticeId);

        for (AttachVo attachVo : attachVoList) {
            if(attachVo.isImage()) {
                setImageUriOf(attachVo);
            }
        }

        noticeDetailResponse.setAttachList(attachVoList); //첨부파일 리스트

        return noticeDetailResponse;
    }

    @Override
    public NoticeDetailResponse getLatestNoticeDetail() {
        NoticeDetailResponse noticeDetailResponse = noticeDao.selectLatestOne();

        if(noticeDetailResponse == null) {
            throw new NoticeNotFoundException(ErrorCode.NOTICE_NOT_FOUND);
        }

        List<AttachVo> attachVoList = noticeAttachDao.selectList(noticeDetailResponse.getNoticeId());

        for (AttachVo attachVo : attachVoList) {
            if(attachVo.isImage()) {
                setImageUriOf(attachVo);
            }
        }

        noticeDetailResponse.setAttachList(attachVoList); //첨부파일 리스트

        return noticeDetailResponse;
    }

    private void setImageUriOf(AttachVo attachVo) {
        attachVo.setThumbnailUri(makeThumbnailUriOf(attachVo));
        attachVo.setOriginalImageUri(makeOriginalImageUriOf(attachVo));
    }

    private String makeOriginalImageUriOf(AttachVo attachVo) {
        return hostUrl +
                "upload" +
                File.separator +
                attachVo.getUploadPath() +
                File.separator +
                attachVo.getUuid() +
                "." +
                attachVo.getExtension();
    }

    private String makeThumbnailUriOf(AttachVo attachVo) {
        return hostUrl +
                "upload" +
                File.separator +
                attachVo.getUploadPath() +
                File.separator +
                attachVo.getUuid() +
                "_thumbnail" +
                "." +
                attachVo.getExtension();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long write(String username, NoticeSaveRequest noticeSaveRequest, List<AttachVo> attachInsertList) {

        NoticeVo noticeVo = NoticeVo.builder()
                .title(noticeSaveRequest.getTitle())
                .content(noticeSaveRequest.getContent())
                .build();

        try {
            long adminId = adminDao.selectAdminIdByUsername(username);
            noticeVo.setAdminId(adminId);
            noticeDao.insertNotice(noticeVo);

            long noticeId = noticeVo.getNoticeId();

            if (attachInsertList != null && !attachInsertList.isEmpty()) {
                insertAttaches(attachInsertList, noticeId); //첨부파일 insert
                updateHasAttach(noticeId); //첨부파일 유무 update
                updateThumbnailUri(attachInsertList, noticeId);
            }

            return noticeId; //등록한 게시물 번호 리턴
        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제하기 위해
            e.printStackTrace();
            throw new NoticeInsertFailedException(attachInsertList, ErrorCode.NOTICE_INSERT_FAILED);
        }
    }

    /**
     * 공지 썸네일 uri 업데이트
     */
    private boolean updateThumbnailUri(List<AttachVo> attachVoList, Long noticeId) {
        for (AttachVo attach : attachVoList) {
            if(attach.isImage()) {
                String thumbnailUri = attach.getUploadPath() + File.separator + attach.getUuid() + "_thumbnail" + "." + attach.getExtension();
                noticeDao.updateThumbnailUri(Map.of("noticeId", noticeId, "thumbnailUri", thumbnailUri));
                return true;
            }
        }
        return false;
    }

    /**
     * 첨부파일 insert
     */
    private void insertAttaches(List<AttachVo> attachInsertList, Long noticeId) {
        for (AttachVo attach : attachInsertList) {
            attach.setNoticeId(noticeId);
            noticeAttachDao.insert(attach);
        }
    }

    /**
     * 첨부파일 delete
     */
    private void deleteAttaches(List<AttachVo> attachDeleteList) {
        for (AttachVo attachVo : attachDeleteList) {
            noticeAttachDao.delete(attachVo.getAttachId());
        }
    }

    /**
     * 첨부파일 유무 여부 update
     */
    private void updateHasAttach(long noticeId) {
        int attachCount = noticeAttachDao.selectCountByNoticeId(noticeId);

        noticeDao.updateHasAttach(
                Map.of(
                        "noticeId", noticeId,
                        "hasAttach", attachCount > 0
                )
        );
    }

    /**
     * 공지 수정
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long noticeId,
                       String content,
                       String title,
                       List<AttachVo> attachInsertList,
                       List<AttachVo> attachDeleteList
    ) {
        if(attachDeleteList != null && attachDeleteList.size() > 0) { //첨부파일 delete
            deleteAttaches(attachDeleteList);
        }

        if(attachInsertList != null && attachInsertList.size() > 0) { //첨부파일 insert
            insertAttaches(attachInsertList, noticeId);
        }

        noticeDao.update(
                Map.of(
                        "noticeId", noticeId,
                        "title", title,
                        "content", content
                )
        );  //게시물 update
        updateHasAttach(noticeId);  //첨부파일 유무 update
        updateThumbnailUri(noticeAttachDao.selectList(noticeId), noticeId);
    }



    /**
     * 공지 삭제
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long noticeId) {
        noticeAttachDao.deleteByNoticeId(noticeId);
        noticeDao.delete(noticeId);
    }
}
