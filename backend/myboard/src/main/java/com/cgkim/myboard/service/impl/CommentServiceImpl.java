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

    @Override
    public List<CommentListResponse> getCommentList(Long boardId) {
        return commentDao.selectList(boardId);
    }

    @Override
    public void writeComment(CommentSaveRequest commentSaveRequest) {
        commentDao.insert(commentSaveRequest);
    }


}