package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.attach.AttachVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 질문 첨부파일 DAO
 */
@Repository
@Mapper
public interface QuestionAttachDao {

    /**
     * 질문 첨부파일 목록 조회
     *
     * @param questionId
     * @return List<AttachVo>
     */
    List<AttachVo> selectListByQuestionId(Long questionId);

    /**
     * 특정 첨부파일 조회
     *
     * @param attachId
     * @return AttachVo
     */
    AttachVo selectOneByAttachId(Long attachId);

    /**
     * 질문에 달린 첨부파일 갯수 조회
     *
     * @param questionId
     * @return int
     */
    int selectCountByQuestionId(Long questionId);

    /**
     * 첨부파일 삽입
     *
     * @param attach
     * @return int
     */
    int insert(AttachVo attach);

    /**
     * 질문에 달린 첨부파일 전체 삭제
     *
     * @param questionId
     */
    void deleteByQuestionId(Long questionId);

    /**
     * 특정 첨부파일 삭제
     *
     * @param attachId
     */
    void deleteByAttachId(Long attachId);
}