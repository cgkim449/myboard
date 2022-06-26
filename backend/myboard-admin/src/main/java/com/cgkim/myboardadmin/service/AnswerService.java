package com.cgkim.myboardadmin.service;

import com.cgkim.myboardadmin.dao.AdminDao;
import com.cgkim.myboardadmin.dao.AnswerAttachDao;
import com.cgkim.myboardadmin.dao.AnswerDao;
import com.cgkim.myboardadmin.exception.AnswerInsertFailedException;
import com.cgkim.myboardadmin.exception.AnswerNotFoundException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.util.AttachURIProvider;
import com.cgkim.myboardadmin.vo.answer.AnswerDetailResponse;
import com.cgkim.myboardadmin.vo.answer.AnswerSaveRequest;
import com.cgkim.myboardadmin.vo.answer.AnswerUpdateRequest;
import com.cgkim.myboardadmin.vo.answer.AnswerVo;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Q&A 답변 Service
 */
@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerDao answerDao;

    private final AnswerAttachDao attachDao;

    private final AdminDao adminDao;

    private final AnswerAttachDao answerAttachDao;

    private final AttachURIProvider attachURIProvider;


    /**
     * 답변 작성
     *
     * @param username
     * @param answerSaveRequest
     * @param attachInsertList
     * @return
     */
    public Long write(String username, AnswerSaveRequest answerSaveRequest, List<AttachVo> attachInsertList) {

        AnswerVo answerVo = AnswerVo.builder()
                .questionId(answerSaveRequest.getQuestionId())
                .title(answerSaveRequest.getTitle())
                .content(answerSaveRequest.getContent())
                .build();

        try {

            Long adminId = adminDao.selectAdminIdByUsername(username);

            answerVo.setAdminId(adminId);
            answerDao.insert(answerVo);

            Long answerId = answerVo.getAnswerId();

            if (isNotEmpty(attachInsertList)) {

                insertAttaches(attachInsertList, answerId);
                updateHasAttach(answerId);
                updateThumbnailUri(attachInsertList, answerId);
            }

            return answerId;

        } catch (Exception e) {

            e.printStackTrace();
            throw new AnswerInsertFailedException(attachInsertList, ErrorCode.ANSWER_INSERT_FAILED);
        }
    }

    private boolean isNotEmpty(List<AttachVo> attachList) {
        return attachList != null && !attachList.isEmpty();
    }

    /**
     * 답변 수정
     *
     * @param answerId
     * @param answerUpdateRequest
     * @param attachInsertList
     * @param attachDeleteList
     */
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long answerId, AnswerUpdateRequest answerUpdateRequest, List<AttachVo> attachInsertList, List<AttachVo> attachDeleteList) {

        if(isNotEmpty(attachDeleteList)) {
            deleteAttaches(attachDeleteList);
        }

        if(isNotEmpty(attachInsertList)) {
            insertAttaches(attachInsertList, answerId);
        }

        answerUpdateRequest.setAnswerId(answerId);

        answerDao.update(answerUpdateRequest);

        updateHasAttach(answerId);
        updateThumbnailUri(answerAttachDao.selectList(answerId), answerId);
    }

    private void updateThumbnailUri(List<AttachVo> attachList, Long answerId) {

        for (AttachVo attach : attachList) {

            if (attach.isImage()) {

                String thumbnailUri = attachURIProvider.createThumbnailURIForDB(attach);
                answerDao.updateThumbnailUri(Map.of("answerId", answerId, "thumbnailUri", thumbnailUri));

                return;
            }
        }

        answerDao.updateThumbnailUri(Map.of("answerId", answerId, "thumbnailUri", ""));
    }

    private void insertAttaches(List<AttachVo> attachInsertList, Long answerId) {

        for (AttachVo attach : attachInsertList) {

            attach.setAnswerId(answerId);
            attachDao.insert(attach);
        }
    }

    private void deleteAttaches(List<AttachVo> attachDeleteList) {

        for (AttachVo attachVo : attachDeleteList) {

            attachDao.delete(attachVo.getAttachId());
        }
    }

    private void updateHasAttach(long answerId) {

        int attachCount = attachDao.selectCountByAnswerId(answerId);
        boolean hasAttach = attachCount > 0;

        answerDao.updateHasAttach(
                Map.of(
                        "answerId", answerId,
                        "hasAttach", hasAttach
                )
        );
    }

    /**
     * 답변 상세조회
     *
     * @param answerId
     * @return AnswerDetailResponse
     */
    public AnswerDetailResponse viewAnswerDetail(Long answerId) {

        AnswerDetailResponse answerDetailResponse = answerDao.selectOne(answerId);

        if(answerDetailResponse == null) {
            throw new AnswerNotFoundException(ErrorCode.ANSWER_NOT_FOUND);
        }

        return answerDetailResponse;
    }

    /**
     * 질문 ID 로 답변 삭제
     *
     * @param questionId
     */
    public void deleteByQuestionId(Long questionId) {
        answerDao.deleteByQuestionId(questionId);
    }

    /**
     * 답변 ID로 답변 삭제
     *
     * @param answerId
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long answerId) {

        attachDao.deleteByAnswerId(answerId);
        answerDao.delete(answerId);
    }
}
