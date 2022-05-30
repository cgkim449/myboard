package com.cgkim.myboard.service.impl;


import com.cgkim.myboard.dao.CommentDao;
import com.cgkim.myboard.service.CommentService;
import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    final CommentDao commentDao;

    /**
     * 특정 게시물의 댓글 리스트
     *
     * @param boardId
     * @return
     */
    @Override
    public List<CommentListResponse> getCommentList(Long boardId) {
        return commentDao.selectList(boardId);
    }

    /**
     * 댓글 작성
     *
     * @param commentSaveRequest
     */
    @Override
    public void writeComment(CommentSaveRequest commentSaveRequest) {
        commentDao.insert(commentSaveRequest);
    }


}