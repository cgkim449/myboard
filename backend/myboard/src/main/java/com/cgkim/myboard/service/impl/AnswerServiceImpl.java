package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.AnswerDao;
import com.cgkim.myboard.exception.BoardInsertFailedException;
import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.service.AnswerService;
import com.cgkim.myboard.vo.answer.AnswerDetailResponse;
import com.cgkim.myboard.vo.answer.AnswerSaveRequest;
import com.cgkim.myboard.vo.answer.AnswerVo;
import com.cgkim.myboard.vo.question.QuestionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerDao answerDao;

    @Override
    public long write(Long adminId, AnswerSaveRequest answerSaveRequest) {
        AnswerVo answerVo = AnswerVo.builder()
                .title(answerSaveRequest.getTitle())
                .content(answerSaveRequest.getContent())
                .questionId(answerSaveRequest.getQuestionId())
                .adminId(adminId)
                .build();

        answerDao.insert(answerVo);//질문 insert
        long id = answerVo.getQuestionId();

        return id;
    }

    @Override
    public AnswerDetailResponse viewDetail(Long id) {
        AnswerDetailResponse answerDetailResponse = answerDao.selectOne(id); //질문
        return answerDetailResponse;
    }
}