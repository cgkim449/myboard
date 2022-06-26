package com.cgkim.myboardadmin.service;


import com.cgkim.myboardadmin.dao.AdminDao;
import com.cgkim.myboardadmin.dao.CommentDao;
import com.cgkim.myboardadmin.vo.comment.CommentListResponse;
import com.cgkim.myboardadmin.vo.comment.CommentSaveRequest;
import com.cgkim.myboardadmin.vo.comment.CommentVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 자유게시판 댓글 Service
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final AdminDao adminDao;

    private final CommentDao commentDao;

    /**
     * 댓글 목록 조회
     *
     * @param boardId
     * @return List<CommentListResponse>
     */
    public List<CommentListResponse> getCommentList(Long boardId) {
        return commentDao.selectList(boardId);
    }

    /**
     * 댓글 작성
     *
     * @param username
     * @param commentSaveRequest
     * @return Long
     */
    public Long writeComment(String username, CommentSaveRequest commentSaveRequest) {

        CommentVo commentVo = CommentVo.builder()
                .boardId(commentSaveRequest.getBoardId())
                .content(commentSaveRequest.getContent())
                .build();

        Long adminId = adminDao.selectAdminIdByUsername(username);

        commentVo.setAdminId(adminId);

        commentDao.insertComment(commentVo);

        return commentVo.getCommentId();
    }

    /**
     * 댓글 삭제
     *
     * @param commentId
     */
    public void delete(Long commentId) {
        commentDao.deleteByCommentId(commentId);
    }
}