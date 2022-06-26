package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.answer.AnswerDetailResponse;
import com.cgkim.myboard.vo.answer.AnswerVo;
import com.cgkim.myboard.vo.question.QuestionDetailResponse;
import com.cgkim.myboard.vo.question.QuestionListResponse;
import com.cgkim.myboard.vo.question.QuestionSearchRequest;
import com.cgkim.myboard.vo.question.QuestionVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Q&A 답변 DAO
 */
@Mapper
@Repository
public interface AnswerDao {

    /**
     * 질문에 달린 답변을 select
     *
     * @param questionId
     * @return AnswerDetailResponse
     */
    AnswerDetailResponse selectByQuestionId(Long questionId);

    /**
     * 답변 한 개를 select
     *
     * @param answerId
     * @return AnswerDetailResponse
     */
    AnswerDetailResponse selectOne(Long answerId);

    /**
     * 질문에 달린 답변의 id 를 select
     *
     * @param questionId
     * @return Long
     */
    Long selectAnswerIdByQuestionId(Long questionId);
}
