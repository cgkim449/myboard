package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.attach.AttachVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 자유게시판 첨부파일 DAO
 */
@Repository
@Mapper
public interface BoardAttachDao {

    /**
     * 게시글에 달린 전체 첨부파일 목록을 select
     *
     * @param boardId
     * @return List<AttachVo>
     */
    List<AttachVo> selectList(Long boardId);

    /**
     * attachId 로 첨부파일 한 개를 select
     *
     * @param attachId
     * @return AttachVo
     */
    AttachVo selectOne(Long attachId);

    /**
     * 게시글에 달린 첨부파일 갯수를 select
     *
     * @param boardId
     * @return int
     */
    int selectCountByBoardId(Long boardId);

    /**
     * 첨부파일을 insert
     *
     * @param attach
     * @return int
     */
    int insert(AttachVo attach);

    /**
     * 게시글에 달린 모든 첨부파일을 delete
     *
     * @param boardId
     */
    void deleteByBoardId(Long boardId);

    /**
     * attachId 로 첨부파일 한 개 delete
     *
     * @param attachId
     */
    void delete(Long attachId);
}