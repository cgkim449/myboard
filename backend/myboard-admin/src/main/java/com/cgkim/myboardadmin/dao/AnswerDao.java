package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.answer.AnswerDetailResponse;
import com.cgkim.myboardadmin.vo.answer.AnswerUpdateRequest;
import com.cgkim.myboardadmin.vo.answer.AnswerVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
     * 답변을 insert
     *
     * @param answerVo
     */
    void insert(AnswerVo answerVo);

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

    /**
     * 질문에 달린 답변 전체 삭제
     *
     * @param questionId
     */
    void deleteByQuestionId(Long questionId);

    /**
     * 답변 한개 삭제
     *
     * @param answerId
     */
    void delete(Long answerId);

    /**
     * 썸네일 URI 업데이트
     *
     * @param answerId
     */
    void updateThumbnailUri(Map<String, Object> answerId);

    /**
     * 첨부파일 유무 업데이트
     *
     * @param updateHasAttachMap
     */
    void updateHasAttach(Map<String, Object> updateHasAttachMap);

    /**
     * 답변 업데이트
     *
     * @param answerUpdateRequest
     */
    void update(AnswerUpdateRequest answerUpdateRequest);
}
