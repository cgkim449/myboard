package com.cgkim.myboardadmin.service;


import com.cgkim.myboardadmin.vo.comment.CommentListResponse;
import com.cgkim.myboardadmin.vo.comment.CommentSaveRequest;
import com.cgkim.myboardadmin.vo.member.GuestSaveRequest;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface CommentService {

    List<CommentListResponse> getCommentList(Long boardId);


    void delete(Long commentId);

    long writeComment(String username, CommentSaveRequest commentSaveRequest);
}
