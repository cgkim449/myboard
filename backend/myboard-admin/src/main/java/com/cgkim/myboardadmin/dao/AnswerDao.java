package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.answer.AnswerDetailResponse;
import com.cgkim.myboardadmin.vo.answer.AnswerVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Map;

@Mapper
@Repository
public interface AnswerDao {

    AnswerDetailResponse selectByQuestionId(Long id);

    void insert(AnswerVo answerVo);

    AnswerDetailResponse selectOne(Long id);

    Long selectAnswerIdByQuestionId(Long questionId);

    void deleteByQuestionId(Long questionId);

    void delete(Long answerId);

    void updateThumbnailUri(Map<String, Object> answerId);

    void updateHasAttach(Map<String, Object> updateHasAttachMap);

    void update(Map<String,Object> answerId);
}
