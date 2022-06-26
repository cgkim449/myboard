package com.cgkim.myboardadmin.service;

import com.cgkim.myboardadmin.dao.QuestionAttachDao;
import com.cgkim.myboardadmin.exception.AttachNotFoundException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Q&A 질문 첨부파일 Service
 */
@RequiredArgsConstructor
@Service
public class QuestionAttachService {

    private final QuestionAttachDao attachDao;

    /**
     * 특정 질문의 첨부파일 리스트
     *
     * @param questionId
     * @return List<AttachVo>
     */
    public List<AttachVo> getList(Long questionId) {
        return attachDao.selectListByQuestionId(questionId);
    }

    /**
     * 특정 첨부파일
     *
     * @param attachId
     * @return AttachVo
     */
    public AttachVo getAttachBy(Long attachId) {

        AttachVo attachVo = attachDao.selectOneByAttachId(attachId);

        if (attachVo == null) {
            throw new AttachNotFoundException(ErrorCode.ATTACH_NOT_FOUND);
        }

        return attachVo;
    }

    /**
     * 첨부파일 id 배열로 첨부파일 리스트 조회
     *
     * @param attachIdArray
     * @return List<AttachVo>
     */
    public List<AttachVo> getList(Long[] attachIdArray) {

        if (attachIdArray == null || attachIdArray.length == 0) {
            return null;
        }

        List<AttachVo> attachVoList = new ArrayList<>();

        for (long attachId : attachIdArray) {
            attachVoList.add(attachDao.selectOneByAttachId(attachId));
        }

        return attachVoList;
    }
}