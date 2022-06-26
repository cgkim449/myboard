package com.cgkim.myboard.service;

import com.cgkim.myboard.dao.AnswerAttachDao;
import com.cgkim.myboard.exception.AttachNotFoundException;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Q&A 답변 Service
 */
@RequiredArgsConstructor
@Service
public class AnswerAttachService {
    private final AnswerAttachDao attachDao;

    /**
     * 특정 첨부파일 조회
     *
     * @param attachId
     * @return AttachVo
     */
    public AttachVo getAttachBy(Long attachId) {

        AttachVo attachVo = attachDao.selectOne(attachId);

        if (attachVo == null) {
            throw new AttachNotFoundException(ErrorCode.ATTACH_NOT_FOUND);
        }

        return attachDao.selectOne(attachId);
    }

}