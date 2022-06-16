package com.cgkim.myboard.service.impl;


import com.cgkim.myboard.dao.AdminDao;
import com.cgkim.myboard.dao.CommentDao;
import com.cgkim.myboard.dao.MemberDao;
import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.exception.GuestPasswordInvalidException;
import com.cgkim.myboard.exception.GuestPasswordMismatchException;
import com.cgkim.myboard.exception.NoAuthorizationException;
import com.cgkim.myboard.service.CommentService;
import com.cgkim.myboard.util.SHA256PasswordEncoder;
import com.cgkim.myboard.vo.board.BoardVo;
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
    private final AdminDao adminDao;
    private final MemberDao memberDao;
    private final CommentDao commentDao;
    private final SHA256PasswordEncoder sha256PasswordEncoder;

    /**
     * 댓글 리스트
     */
    @Override
    public List<CommentListResponse> getCommentList(Long boardId) {
        return commentDao.selectList(boardId);
    }


    /**
     * 회원 및 관리자 댓글 작성
     */
    @Override
    public long writeComment(String username, boolean isAdmin, CommentSaveRequest commentSaveRequest) {
        CommentVo commentVo = CommentVo.builder()
                .boardId(commentSaveRequest.getBoardId())
                .content(commentSaveRequest.getContent())
                .build();
        if(isAdmin) {
            long adminId = adminDao.selectAdminIdByUsername(username);
            commentVo.setAdminId(adminId);
            commentDao.insertAdminComment(commentVo);
        } else {
            long memberId = memberDao.selectMemberIdByUsername(username);
            commentVo.setMemberId(memberId);
            commentDao.insertMemberComment(commentVo);
        }
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

    @Override
    public void checkOwner(Long commentId, String username, String guestPassword) throws NoSuchAlgorithmException {
        if(isAnonymous(commentId)) { //익명 글이면
            validateGuestPassword(guestPassword); //비밀번호 유효성 검증
            checkGuestPassword(commentId, guestPassword); //비밀번호 체크
        } else {
            if(!memberDao.selectMemberIdByUsername(username).equals(commentDao.selectMemberId(commentId))) {
                throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
            }
        }
    }
    private void validateGuestPassword(String guestPassword) {
        if(guestPassword == null || guestPassword.equals("")) {
            throw new GuestPasswordInvalidException(ErrorCode.GUEST_PASSWORD_INVALID);
        }
    }
}