package com.cgkim.myboardadmin.service;

import com.cgkim.myboardadmin.dao.AdminDao;
import com.cgkim.myboardadmin.dao.FAQAttachDao;
import com.cgkim.myboardadmin.dao.FAQDao;
import com.cgkim.myboardadmin.exception.FAQInsertFailedException;
import com.cgkim.myboardadmin.exception.FAQNotFoundException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.util.AttachURIProvider;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.faq.FAQDetailResponse;
import com.cgkim.myboardadmin.vo.faq.FAQListResponse;
import com.cgkim.myboardadmin.vo.faq.FAQSaveRequest;
import com.cgkim.myboardadmin.vo.faq.FAQUpdateRequest;
import com.cgkim.myboardadmin.vo.faq.FAQVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * FAQ Service
 */
@RequiredArgsConstructor
@Service
public class FAQService {

    private final FAQDao faqDao;

    private final AdminDao adminDao;

    private final FAQAttachDao faqAttachDao;

    private final AttachURIProvider attachURIProvider;

    /**
     * FAQ 목록 조회
     *
     * @param categoryId
     * @return List<FAQListResponse>
     */
    public List<FAQListResponse> getList(Integer categoryId) {

        return faqDao.selectList(categoryId);
    }

    /**
     * FAQ 상세 조회
     *
     * @param faqId
     * @return BoardDetailResponse
     */
    public FAQDetailResponse viewFAQDetail(Long faqId) {

        FAQDetailResponse faqDetailResponse = faqDao.selectOne(faqId);

        if(faqDetailResponse == null) {
            throw new FAQNotFoundException(ErrorCode.FAQ_NOT_FOUND);
        }

        List<AttachVo> faqAttachList = faqAttachDao.selectList(faqId);

        attachURIProvider.setImageURIsOf(faqAttachList);

        faqDetailResponse.setAttachList(faqAttachList);

        return faqDetailResponse;
    }

    /**
     * FAQ 작성
     *
     * @param username
     * @param faqSaveRequest
     * @param attachInsertList
     * @return Long
     */
    @Transactional(rollbackFor = Exception.class)
    public Long write(String username, FAQSaveRequest faqSaveRequest, List<AttachVo> attachInsertList) {

        FAQVo faqVo = FAQVo.builder()
                .categoryId(faqSaveRequest.getCategoryId())
                .title(faqSaveRequest.getTitle())
                .content(faqSaveRequest.getContent())
                .build();

        try {

            Long adminId = adminDao.selectAdminIdByUsername(username);

            faqVo.setAdminId(adminId);
            faqDao.insertFAQ(faqVo);

            Long faqId = faqVo.getFaqId();

            if (attachInsertList != null && !attachInsertList.isEmpty()) {

                insertAttaches(attachInsertList, faqId);
                updateHasAttach(faqId);
                updateThumbnailUri(attachInsertList, faqId);
            }

            return faqId;

        } catch (Exception e) {

            e.printStackTrace();
            throw new FAQInsertFailedException(attachInsertList, ErrorCode.FAQ_INSERT_FAILED);
        }
    }

    private void updateThumbnailUri(List<AttachVo> attachList, Long faqId) {

        for (AttachVo attach : attachList) {

            if (attach.isImage()) {

                String thumbnailUri = attachURIProvider.createThumbnailURIForDB(attach);
                faqDao.updateThumbnailUri(Map.of("faqId", faqId, "thumbnailUri", thumbnailUri));

                return;
            }
        }

        faqDao.updateThumbnailUri(Map.of("faqId", faqId, "thumbnailUri", ""));
    }

    private void insertAttaches(List<AttachVo> attachInsertList, Long faqId) {

        for (AttachVo attach : attachInsertList) {

            attach.setFaqId(faqId);
            faqAttachDao.insert(attach);
        }
    }

    private void updateHasAttach(long faqId) {

        int attachCount = faqAttachDao.selectCountByFAQId(faqId);
        boolean hasAttach = attachCount > 0;

        faqDao.updateHasAttach(
                Map.of(
                        "faqId", faqId,
                        "hasAttach", hasAttach
                )
        );
    }

    /**
     * FAQ 삭제
     *
     * @param faqId
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long faqId) {

        faqAttachDao.deleteByFAQId(faqId);
        faqDao.delete(faqId);
    }

    /**
     * FAQ 수정
     *
     * @param faqId
     * @param faqUpdateRequest
     * @param attachInsertList
     * @param attachDeleteList
     */
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long faqId,
                       FAQUpdateRequest faqUpdateRequest,
                       List<AttachVo> attachInsertList,
                       List<AttachVo> attachDeleteList
    ) {

        if(isNotEmpty(attachDeleteList)) {

            deleteAttaches(attachDeleteList);
        }

        if(isNotEmpty(attachInsertList)) {

            insertAttaches(attachInsertList, faqId);
        }

        faqUpdateRequest.setFaqId(faqId);

        faqDao.update(faqUpdateRequest);
        updateHasAttach(faqId);
        updateThumbnailUri(faqAttachDao.selectList(faqId), faqId);
    }

    private boolean isNotEmpty(List<AttachVo> attachList) {

        return attachList != null && !attachList.isEmpty();
    }

    private void deleteAttaches(List<AttachVo> attachDeleteList) {

        for (AttachVo attachVo : attachDeleteList) {

            faqAttachDao.delete(attachVo.getAttachId());
        }
    }
}
