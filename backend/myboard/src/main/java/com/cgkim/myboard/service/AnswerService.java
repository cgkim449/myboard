package com.cgkim.myboard.service;

import com.cgkim.myboard.dao.AnswerDao;
import com.cgkim.myboard.exception.AnswerNotFoundException;
import com.cgkim.myboard.exception.BoardNotFoundException;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.vo.answer.AnswerDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Q&A 답변 Service
 */
@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerDao answerDao;


    /**
     * 답변 상세 조회
     *
     * @param answerId
     * @return AnswerDetailResponse
     */
    public AnswerDetailResponse viewAnswerDetail(Long answerId) {

        AnswerDetailResponse answerDetailResponse = answerDao.selectOne(answerId);

        if (answerDetailResponse == null) {
            throw new AnswerNotFoundException(ErrorCode.ANSWER_NOT_FOUND);
        }

        return answerDetailResponse;
    }
}
