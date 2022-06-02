package com.cgkim.myboard.service;

import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import com.cgkim.myboard.vo.user.GuestSaveRequest;

import java.util.List;

public interface CommentService {

    List<CommentListResponse> getCommentList(Long boardId);

    void writeComment(Long userId, CommentSaveRequest commentSaveRequest);

    void writeComment(GuestSaveRequest guestSaveRequest, CommentSaveRequest commentSaveRequest);

    void delete(Long commentId);

    boolean checkAnonymous(Long commentId);

    void checkGuestPassword(Long commentId, String guestPassword);
}
