package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.AnswerAttachDao;
import com.cgkim.myboard.service.AttachService;
import com.cgkim.myboard.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnswerAttachServiceImpl implements AttachService {
    private final AnswerAttachDao attachDao;

    /**
     * 특정 게시물의 첨부파일 리스트
     */
    @Override
    public List<AttachVo> getList(Long boardId) {
        return attachDao.selectList(boardId);
    }

    /**
     * 첨부파일 한개 select
     */
    @Override
    public AttachVo get(Long attachId) {
        return attachDao.selectOne(attachId);
    }

    /**
     * 첨부파일 id로 첨부파일 select
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