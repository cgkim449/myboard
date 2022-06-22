package com.cgkim.myboardadmin.service.impl;

import com.cgkim.myboardadmin.dao.AnswerDao;
import com.cgkim.myboardadmin.service.AnswerService;
import com.cgkim.myboardadmin.vo.answer.AnswerDetailResponse;
import com.cgkim.myboardadmin.vo.answer.AnswerSaveRequest;
import com.cgkim.myboardadmin.vo.answer.AnswerVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerDao answerDao;

    @Override
    public Long write(Long adminId, AnswerSaveRequest answerSaveRequest) {
        AnswerVo answerVo = AnswerVo.builder()
                .title(answerSaveRequest.getTitle())
                .content(answerSaveRequest.getContent())
                .questionId(answerSaveRequest.getQuestionId())
                .adminId(adminId)
                .build();

        answerDao.insert(answerVo);//질문 insert
        long answerId = answerVo.getQuestionId();

        return answerId;
    }

    @Override
    public AnswerDetailResponse viewDetail(Long id) {
        AnswerDetailResponse answerDetailResponse = answerDao.selectOne(id); //질문
        return answerDetailResponse;
    }
}
