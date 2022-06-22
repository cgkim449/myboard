package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.answer.AnswerDetailResponse;
import com.cgkim.myboardadmin.vo.answer.AnswerVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AnswerDao {

    AnswerVo selectByQuestionId(Long id);

    void insert(AnswerVo answerVo);

    AnswerDetailResponse selectOne(Long id);

    Long selectAnswerIdByQuestionId(Long questionId);
}
