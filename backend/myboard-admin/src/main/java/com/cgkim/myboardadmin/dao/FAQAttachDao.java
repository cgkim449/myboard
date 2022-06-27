package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.attach.AttachVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FAQ 첨부파일 DAO
 */
@Repository
@Mapper
public interface FAQAttachDao {

    /**
     * 첨부파일 목록 조회
     * @param faqId
     * @return List<AttachVo>
     */
    List<AttachVo> selectList(Long faqId);

    /**
     * 특정 첨부파일 조회
     *
     * @param attachId
     * @return AttachVo
     */
    AttachVo selectOne(Long attachId);

    /**
     * 첨부파일 갯수 조회
     *
     * @param faqId
     * @return int
     */
    int selectCountByFAQId(Long faqId);

    /**
     * 첨부파일 삽입
     *
     * @param attach
     * @return int
     */
    int insert(AttachVo attach);

    /**
     * FAQ 에 달린 첨부파일 전체 삭제
     *
     * @param faqId
     */
    void deleteByFAQId(Long faqId);

    /**
     * 특정 첨부파일 삭제
     *
     * @param attachId
     */
    void delete(Long attachId);
}