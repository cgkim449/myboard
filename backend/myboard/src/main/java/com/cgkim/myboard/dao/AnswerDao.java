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

@Mapper
@Repository
public interface AnswerDao {

    AnswerVo selectByQuestionId(Long id);

    void insert(AnswerVo answerVo);

    AnswerDetailResponse selectOne(Long id);
}
