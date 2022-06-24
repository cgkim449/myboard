package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.AnswerAttachDao;
import com.cgkim.myboard.service.AttachService;
import com.cgkim.myboard.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Q&A 답변 Service
 */
@RequiredArgsConstructor
@Service
public class AnswerAttachServiceImpl implements AttachService {
    private final AnswerAttachDao attachDao;

    /**
     * 특정 게시물의 첨부파일 목록
     * @param boardId
     * @return
     */
    @Override
    public List<AttachVo> getList(Long boardId) {
        return attachDao.selectList(boardId);
    }

    /**
     * 특정 첨부파일 select
     * @param attachId
     * @return
     */
    @Override
    public AttachVo get(Long attachId) {
        return attachDao.selectOne(attachId);
    }

    /**
     * 첨부파일 리스트 select
     * @param attachIdArray
     * @return
     */
    @Override
    public List<AttachVo> getList(Long[] attachIdArray) {
        if(attachIdArray == null || attachIdArray.length == 0) {
            return null;
        }

        List<AttachVo> attachVoList = new ArrayList<>();

        for (long attachId : attachIdArray) {
            attachVoList.add(attachDao.selectOne(attachId));
        }

        return attachVoList;
    }
}