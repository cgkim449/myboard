package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.comment.CommentListResponse;
import com.cgkim.myboardadmin.vo.comment.CommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 댓글 DAO
 */
@Repository
@Mapper
public interface CommentDao {

    /**
     * 댓글 목록
     *
     * @param boardId
     * @return List<CommentListResponse>
     */
    List<CommentListResponse> selectList(Long boardId);

    /**
     * 게시글에 달린 댓글 전체 삭제
     *
     * @param boardId
     */
    void deleteByBoardId(Long boardId);

    /**
     * 댓글 삭제
     *
     * @param commentId
     */
    void deleteByCommentId(Long commentId);

    /**
     * 댓글 작성
     *
     * @param commentVo
     * @return Long
     */
    Long insertComment(CommentVo commentVo);
}