package com.cgkim.myboard.service;

import com.cgkim.myboard.dao.QuestionAttachDao;
import com.cgkim.myboard.exception.AttachNotFoundException;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Q&A 질문 Service
 */
@RequiredArgsConstructor
@Service
public class QuestionAttachService {

    private final QuestionAttachDao attachDao;

    /**
     * 특정 질문의 첨부파일 리스트 조회
     *
     * @param questionId
     * @return List<AttachVo>
     */
    public List<AttachVo> getList(Long questionId) {
        return attachDao.selectListByQuestionId(questionId);
    }

    /**
     * 특정 첨부파일 조회
     *
     * @param attachId
     * @return
     */
    public AttachVo getAttachBy(Long attachId) {
        AttachVo attachVo = attachDao.selectOneByAttachId(attachId);

        if(attachVo == null) {
            throw new AttachNotFoundException(ErrorCode.ATTACH_NOT_FOUND);
        }

        return attachVo;
    }

    /**
     *
     *
     * @param attachIdArray
     * @return List<AttachVo>
     */
    public List<AttachVo> getList(Long[] attachIdArray) {

        if(attachIdArray == null || attachIdArray.length == 0) {
            return null;
        }

        List<AttachVo> attachVoList = new ArrayList<>();

        for (Long attachId : attachIdArray) {
            attachVoList.add(attachDao.selectOneByAttachId(attachId));
        }

        return attachVoList;
    }
}