package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.attach.AttachVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 공지 첨부 DAO
 */
@Repository
@Mapper
public interface NoticeAttachDao {

    /**
     * 공지의 첨부파일 목록 조회
     *
     * @param noticeId
     * @return List<AttachVo>
     */
    List<AttachVo> selectList(Long noticeId);

    /**
     * 특정 첨부파일 조회
     *
     * @param attachId
     * @return AttachVo
     */
    AttachVo selectOne(Long attachId);

    /**
     * 공지에 달린 첨부파일 갯수 조회
     *
     * @param noticeId
     * @return int
     */
    int selectCountByNoticeId(Long noticeId);

    /**
     * 첨부파일 삽입
     *
     * @param attach
     * @return int
     */
    int insert(AttachVo attach);

    /**
     * 공지에 달린 첨부파일 전체 삭제
     * @param noticeId
     */
    void deleteByNoticeId(Long noticeId);

    /**
     * 특정 첨부파일 삭제
     *
     * @param attachId
     */
    void delete(Long attachId);

}