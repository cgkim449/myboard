package com.cgkim.myboardadmin.service.impl;

import com.cgkim.myboardadmin.dao.AdminDao;
import com.cgkim.myboardadmin.dao.AnswerAttachDao;
import com.cgkim.myboardadmin.dao.AnswerDao;
import com.cgkim.myboardadmin.exception.AnswerInsertFailedException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.service.AnswerService;
import com.cgkim.myboardadmin.vo.answer.AnswerDetailResponse;
import com.cgkim.myboardadmin.vo.answer.AnswerSaveRequest;
import com.cgkim.myboardadmin.vo.answer.AnswerVo;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerDao answerDao;
    private final AnswerAttachDao attachDao;
    private final AdminDao adminDao;
    private final AnswerAttachDao answerAttachDao;


    @Override
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

            if (attachInsertList != null && !attachInsertList.isEmpty()) {
                insertAttaches(attachInsertList, answerId); //첨부파일 insert
                updateHasAttach(answerId); //첨부파일 유무 update
                updateThumbnailUri(attachInsertList, answerId);
            }

            return answerId; //등록한 게시물 번호 리턴
        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제하기 위해
            e.printStackTrace();
            throw new AnswerInsertFailedException(attachInsertList, ErrorCode.ANSWER_INSERT_FAILED);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void modify(Long answerId, String content, String title, List<AttachVo> attachInsertList, List<AttachVo> attachDeleteList) {
        if(attachDeleteList != null && attachDeleteList.size() > 0) { //첨부파일 delete
            deleteAttaches(attachDeleteList);
        }

        if(attachInsertList != null && attachInsertList.size() > 0) { //첨부파일 insert
            insertAttaches(attachInsertList, answerId);
        }

        answerDao.update(
                Map.of(
                        "answerId", answerId,
                        "title", title,
                        "content", content
                )
        );  //게시물 update
        updateHasAttach(answerId);  //첨부파일 유무 update
        updateThumbnailUri(answerAttachDao.selectList(answerId), answerId);
    }

    /**
     * 게시물 썸네일 uri 업데이트
     */
    private boolean updateThumbnailUri(List<AttachVo> attachVoList, Long answerId) {
        for (AttachVo attach : attachVoList) {
            if(attach.isImage()) {
                String thumbnailUri = attach.getUploadPath() + File.separator + attach.getUuid() + "_thumbnail" + "." + attach.getExtension();
                answerDao.updateThumbnailUri(Map.of("answerId", answerId, "thumbnailUri", thumbnailUri));
                return true;
            }
        }
        return false;
    }

    /**
     * 첨부파일 insert
     */
    private void insertAttaches(List<AttachVo> attachInsertList, Long id) {
        for (AttachVo attach : attachInsertList) {
            attach.setAnswerId(id);
            attachDao.insert(attach);
        }
    }

    /**
     * 첨부파일 delete
     */
    private void deleteAttaches(List<AttachVo> attachDeleteList) {
        for (AttachVo attachVo : attachDeleteList) {
            attachDao.delete(attachVo.getAttachId());
        }
    }

    /**
     * 첨부파일 유무 여부 update
     */
    private void updateHasAttach(long answerId) {
        int attachCount = attachDao.selectCountByAnswerId(answerId);

        answerDao.updateHasAttach(
                Map.of(
                        "answerId", answerId,
                        "hasAttach", attachCount > 0
                )
        );
    }


    @Override
    public AnswerDetailResponse viewDetail(Long id) {
        AnswerDetailResponse answerDetailResponse = answerDao.selectOne(id); //질문
        return answerDetailResponse;
    }

    @Override
    public void deleteByQuestionId(Long questionId) {
        answerDao.deleteByQuestionId(questionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long answerId) {
        attachDao.deleteByAnswerId(answerId);
        answerDao.delete(answerId);
    }
}
