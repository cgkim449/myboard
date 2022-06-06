package com.cgkim.myboard.service;

import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import com.cgkim.myboard.vo.member.GuestSaveRequest;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface CommentService {

    List<CommentListResponse> getCommentList(Long boardId);

    long writeComment(Long memberId, CommentSaveRequest commentSaveRequest);

    long writeComment(GuestSaveRequest guestSaveRequest, CommentSaveRequest commentSaveRequest) throws NoSuchAlgorithmException;

    void delete(Long commentId);

    boolean isAnonymous(Long commentId);

    void checkGuestPassword(Long commentId, String guestPassword) throws NoSuchAlgorithmException;
}
