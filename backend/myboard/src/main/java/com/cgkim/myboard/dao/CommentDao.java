package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.cgkim.myboard.vo.comment.CommentVo;
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
     * 댓글 목록 조회
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
     * 회원 댓글 삽입
     *
     * @param commentVo
     */
    void insertMemberComment(CommentVo commentVo);

    /**
     * 관리자 댓글 삽입
     *
     * @param commentVo
     */
    void insertAdminComment(CommentVo commentVo);

    /**
     * 익명 댓글 삽입
     *
     * @param commentVo
     */
    void insertGuestComment(CommentVo commentVo);

    /**
     * 댓글 삭제
     *
     * @param commentId
     */
    void deleteByCommentId(Long commentId);

    /**
     * 댓글 작성자(회원) ID 조회
     *
     * @param commentId
     * @return Long
     */
    Long selectMemberId(Long commentId);

    /**
     * 익명 댓글 비밀번호 검증
     *
     * @param guestPasswordCheckMap
     * @return Long
     */
    Long selectOneByGuestPassword(Map<String, Object> guestPasswordCheckMap);

    /**
     * 익명 댓글 작성자 별명 조회
     *
     * @param commentId
     * @return String
     */
    String selectGuestNickname(Long commentId);

    /**
     * 댓글 작성자(관리자) ID 조회
     *
     * @param commentId
     * @return Long
     */
    Long selectAdminId(Long commentId);

}