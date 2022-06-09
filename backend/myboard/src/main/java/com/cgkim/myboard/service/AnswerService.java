package com.cgkim.myboard.service;

import com.cgkim.myboard.vo.answer.AnswerDetailResponse;
import com.cgkim.myboard.vo.answer.AnswerSaveRequest;
import org.springframework.transaction.annotation.Transactional;

public interface AnswerService {
    long write(Long answerId, AnswerSaveRequest answerSaveRequest);

    AnswerDetailResponse viewDetail(Long id);
}
