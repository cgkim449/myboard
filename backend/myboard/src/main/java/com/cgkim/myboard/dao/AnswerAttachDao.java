package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.attach.AttachVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Q&A 답변 첨부파일 DAO
 */
@Repository
@Mapper
public interface AnswerAttachDao {

    /**
     * 답변에 달린 전체 첨부파일 목록을 select
     *
     * @param answerId
     * @return List<AttachVo>
     */
    List<AttachVo> selectList(Long answerId);

    /**
     * attachId 로 첨부파일 한 개를 select
     *
     * @param attachId
     * @return AttachVo
     */
    AttachVo selectOne(Long attachId);
}