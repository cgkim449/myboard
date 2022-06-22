package com.cgkim.myboardadmin.service;


import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.question.QuestionDetailResponse;
import com.cgkim.myboardadmin.vo.question.QuestionListResponse;
import com.cgkim.myboardadmin.vo.question.QuestionSaveRequest;
import com.cgkim.myboardadmin.vo.question.QuestionSearchRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionService {
    List<QuestionListResponse> getQuestionList(QuestionSearchRequest questionSearchRequest);

    int getTotalCount(QuestionSearchRequest questionSearchRequest);

    @Transactional(rollbackFor = Exception.class)
    long write(String username, QuestionSaveRequest questionSaveRequest, List<AttachVo> attachInsertList);

    QuestionDetailResponse viewDetail(Long id);

    @Transactional(rollbackFor = Exception.class)
    void delete(Long questionId);

    void checkOwner(Long questionId, String username);

    void checkHasAnswer(Long questionId);
    @Transactional(rollbackFor = Exception.class)
    void modify(Long questionId, String content, String title, Integer isSecret, List<AttachVo> attachInsertList, List<AttachVo> attachDeleteList);
}
