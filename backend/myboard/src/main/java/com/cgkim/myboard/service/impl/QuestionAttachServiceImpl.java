package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.QuestionAttachDao;
import com.cgkim.myboard.service.AttachService;
import com.cgkim.myboard.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class QuestionAttachServiceImpl implements AttachService {
    private final QuestionAttachDao questionAttachDao;

    /**
     * 특정 질문의 첨부파일 리스트
     */
    @Override
    public List<AttachVo> getList(Long questionId) {
        return questionAttachDao.selectList(questionId);
    }

    /**
     * 첨부파일 한개 select
     */
    @Override
    public AttachVo get(Long attachId) {
        return questionAttachDao.selectOne(attachId);
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
            attachVoList.add(questionAttachDao.selectOne(attachId));
        }

        return attachVoList;
    }
}