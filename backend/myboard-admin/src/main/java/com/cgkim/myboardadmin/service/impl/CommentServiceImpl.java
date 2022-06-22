package com.cgkim.myboardadmin.service.impl;


import com.cgkim.myboardadmin.dao.AdminDao;
import com.cgkim.myboardadmin.dao.CommentDao;
import com.cgkim.myboardadmin.service.CommentService;
import com.cgkim.myboardadmin.vo.comment.CommentListResponse;
import com.cgkim.myboardadmin.vo.comment.CommentSaveRequest;
import com.cgkim.myboardadmin.vo.comment.CommentVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final AdminDao adminDao;
    private final CommentDao commentDao;

    /**
     * 댓글 리스트
     */
    @Override
    public List<CommentListResponse> getCommentList(Long boardId) {
        return commentDao.selectList(boardId);
    }

    /**
     * 댓글 작성
     */
    @Override
    public long writeComment(String username, CommentSaveRequest commentSaveRequest) {

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
     */
    @Override
    public void delete(Long commentId) {
        commentDao.deleteByCommentId(commentId);
    }
}