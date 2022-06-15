package com.cgkim.myboard.service.impl;


import com.cgkim.myboard.dao.CommentDao;
import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.exception.GuestPasswordMismatchException;
import com.cgkim.myboard.service.CommentService;
import com.cgkim.myboard.util.SHA256PasswordEncoder;
import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import com.cgkim.myboard.vo.comment.CommentVo;
import com.cgkim.myboard.vo.member.GuestSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    final CommentDao commentDao;
    final SHA256PasswordEncoder sha256PasswordEncoder;

    /**
     * 댓글 리스트
     */
    @Override
    public List<CommentListResponse> getCommentList(Long boardId) {
        return commentDao.selectList(boardId);
    }

    /**
     * 회원 댓글 작성
     */
    @Override
    public long writeComment(Long memberId, CommentSaveRequest commentSaveRequest) {
        CommentVo commentVo = CommentVo.builder()
                                    .boardId(commentSaveRequest.getBoardId())
                                    .content(commentSaveRequest.getContent())
                                    .memberId(memberId)
                                    .build();
        commentDao.insertMemberComment(commentVo);
        return commentVo.getCommentId();
    }

    /**
     * 익명 댓글 작성
     */
    @Override
    public long writeComment(GuestSaveRequest guestSaveRequest, CommentSaveRequest commentSaveRequest) throws NoSuchAlgorithmException {
        CommentVo commentVo = CommentVo.builder()
                                    .boardId(commentSaveRequest.getBoardId())
                                    .content(commentSaveRequest.getContent())
                                    .guestNickname(guestSaveRequest.getGuestNickname())
                                    .guestPassword(sha256PasswordEncoder.getHash(guestSaveRequest.getGuestPassword()))
                                    .build();
        commentDao.insertGuestComment(commentVo);
        return commentVo.getCommentId();
    }

    /**
     * 댓글 삭제
     */
    @Override
    public void delete(Long commentId) {
        commentDao.deleteByCommentId(commentId);
    }

    /**
     * 익명 댓글 여부 확인
     */
    @Override
    public boolean isAnonymous(Long commentId) {
        return commentDao.selectMemberId(commentId) == null;
    }

    /**
     * 익명 댓글 비밀번호 확인
     */
    @Override
    public void checkGuestPassword(Long commentId, String guestPassword) throws NoSuchAlgorithmException {
        Long result = commentDao.selectOneByGuestPassword(
                Map.of(
                        "commentId", commentId,
                        "guestPassword", sha256PasswordEncoder.getHash(guestPassword)
                )
        );
        if(result == null) {
            throw new GuestPasswordMismatchException(ErrorCode.GUEST_PASSWORD_MISMATCH);
        }
    }
}