package com.cgkim.myboardadmin.service;


import com.cgkim.myboardadmin.vo.answer.AnswerDetailResponse;
import com.cgkim.myboardadmin.vo.answer.AnswerSaveRequest;

public interface AnswerService {
    Long write(Long answerId, AnswerSaveRequest answerSaveRequest);

    AnswerDetailResponse viewDetail(Long id);
}
