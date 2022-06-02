package com.cgkim.myboard.service.impl;


import com.cgkim.myboard.dao.CommentDao;
import com.cgkim.myboard.service.CommentService;
import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import com.cgkim.myboard.vo.comment.CommentVo;
import com.cgkim.myboard.vo.user.GuestSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    final CommentDao commentDao;

    /**
     * 댓글 리스트
     */
    @Override
    public List<CommentListResponse> getCommentList(Long boardId) {
        return commentDao.selectList(boardId);
    }

    @Override
    public void writeComment(Long userId, CommentSaveRequest commentSaveRequest) {

        commentDao.insertUserComment(
                CommentVo.builder()
                        .boardId(commentSaveRequest.getBoardId())
                        .commentContent(commentSaveRequest.getCommentContent())
                        .userId(userId)
                        .build()
        );
    }

    @Override
    public void writeComment(GuestSaveRequest guestSaveRequest, CommentSaveRequest commentSaveRequest) {
        commentDao.insertGuestComment(
                CommentVo.builder()
                        .boardId(commentSaveRequest.getBoardId())
                        .commentContent(commentSaveRequest.getCommentContent())
                        .guestNickname(guestSaveRequest.getGuestNickname())
                        .guestPassword(guestSaveRequest.getGuestPassword())
                        .build()
        );
    }
}