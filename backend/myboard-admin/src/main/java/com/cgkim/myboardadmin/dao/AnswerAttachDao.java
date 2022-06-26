package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.attach.AttachVo;
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

    /**
     * 답변에 달린 첨부파일 갯수 select
     *
     * @param answerId
     * @return int
     */
    int selectCountByAnswerId(Long answerId);

    /**
     * 첨부파일 insert
     *
     * @param attach
     * @return int
     */
    int insert(AttachVo attach);

    /**
     * 답변에 달린 첨부파일 전체 삭제
     *
     * @param answerId
     */
    void deleteByAnswerId(Long answerId);

    /**
     * 첨부파일 한 개 삭제
     *
     * @param attachId
     */
    void delete(Long attachId);
}