package com.cgkim.myboardadmin.service.impl;

import com.cgkim.myboardadmin.dao.AdminDao;
import com.cgkim.myboardadmin.dao.FAQAttachDao;
import com.cgkim.myboardadmin.dao.FAQDao;
import com.cgkim.myboardadmin.exception.FAQInsertFailedException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.service.FAQService;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.faq.FAQListResponse;
import com.cgkim.myboardadmin.vo.faq.FAQSaveRequest;
import com.cgkim.myboardadmin.vo.faq.FAQVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class FAQServiceImpl implements FAQService {
    private final FAQDao faqDao;
    private final AdminDao adminDao;
    private final FAQAttachDao faqAttachDao;

    @Override
    public List<FAQListResponse> getList(Integer categoryId) {
        List<FAQListResponse> faqList = faqDao.selectList(categoryId);
        return faqList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long write(String username, FAQSaveRequest faqSaveRequest, List<AttachVo> attachInsertList) {

        FAQVo faqVo = FAQVo.builder()
                .categoryId(faqSaveRequest.getCategoryId())
                .title(faqSaveRequest.getTitle())
                .content(faqSaveRequest.getContent())
                .build();

        try {
            long adminId = adminDao.selectAdminIdByUsername(username);

            faqVo.setAdminId(adminId);
            faqDao.insertFAQ(faqVo);

            long faqId = faqVo.getFaqId();

            if (attachInsertList != null && !attachInsertList.isEmpty()) {
                insertAttaches(attachInsertList, faqId); //첨부파일 insert
                updateHasAttach(faqId); //첨부파일 유무 update
                updateThumbnailUri(attachInsertList, faqId);
            }

            return faqId; //등록한 게시물 번호 리턴
        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제하기 위해
            e.printStackTrace();
            throw new FAQInsertFailedException(attachInsertList, ErrorCode.FAQ_INSERT_FAILED);
        }
    }

    /**
     * 게시물 썸네일 uri 업데이트
     */
    private boolean updateThumbnailUri(List<AttachVo> attachVoList, Long faqId) {
        for (AttachVo attach : attachVoList) {
            if(attach.isImage()) {
                String thumbnailUri = attach.getUploadPath() + File.separator + attach.getUuid() + "_thumbnail" + "." + attach.getExtension();
                faqDao.updateThumbnailUri(Map.of("faqId", faqId, "thumbnailUri", thumbnailUri));
                return true;
            }
        }
        return false;
    }

    /**
     * 첨부파일 insert
     */
    private void insertAttaches(List<AttachVo> attachInsertList, Long faqId) {
        for (AttachVo attach : attachInsertList) {
            attach.setFaqId(faqId);
            faqAttachDao.insert(attach);
        }
    }

    /**
     * 첨부파일 delete
     */
    private void deleteAttaches(List<AttachVo> attachDeleteList) {
        for (AttachVo attachVo : attachDeleteList) {
            faqAttachDao.delete(attachVo.getAttachId());
        }
    }

    /**
     * 첨부파일 유무 여부 update
     */
    private void updateHasAttach(long faqId) {
        int attachCount = faqAttachDao.selectCountByFAQId(faqId);

        faqDao.updateHasAttach(
                Map.of(
                        "faqId", faqId,
                        "hasAttach", attachCount > 0
                )
        );
    }
}
