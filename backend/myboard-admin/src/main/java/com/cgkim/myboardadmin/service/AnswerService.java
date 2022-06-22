package com.cgkim.myboardadmin.service;


import com.cgkim.myboardadmin.vo.answer.AnswerDetailResponse;
import com.cgkim.myboardadmin.vo.answer.AnswerSaveRequest;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AnswerService {

    AnswerDetailResponse viewDetail(Long id);

    void deleteByQuestionId(Long questionId);

    @Transactional(rollbackFor = Exception.class)
    void delete(Long answerId);

    @Transactional(rollbackFor = Exception.class)
    Long write(String username, AnswerSaveRequest answerSaveRequest, List<AttachVo> attachInsertList);

    @Transactional(rollbackFor = Exception.class)
    void modify(Long answerId, String content, String title, List<AttachVo> attachInsertList, List<AttachVo> attachDeleteList);
}
