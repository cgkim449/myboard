package com.cgkim.myboard.service;

import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.cgkim.myboard.vo.comment.CommentSaveRequest;

import java.util.List;

public interface CommentService {
    void writeComment(CommentSaveRequest commentSaveRequest);

    List<CommentListResponse> getCommentList(Long boardId);
}
