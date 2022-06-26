package com.cgkim.myboard.service;


import com.cgkim.myboard.dao.CommentDao;
import com.cgkim.myboard.dao.MemberDao;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.exception.GuestPasswordInvalidException;
import com.cgkim.myboard.exception.GuestPasswordMismatchException;
import com.cgkim.myboard.exception.LoginRequiredException;
import com.cgkim.myboard.exception.NoAuthorizationException;
import com.cgkim.myboard.util.SHA256PasswordEncoder;
import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import com.cgkim.myboard.vo.comment.CommentVo;
import com.cgkim.myboard.vo.common.GuestPasswordCheckRequest;
import com.cgkim.myboard.vo.common.GuestSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * 자유게시판 댓글 Service
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberDao memberDao;

    private final CommentDao commentDao;

    private final SHA256PasswordEncoder sha256PasswordEncoder;

    /**
     * 댓글 목록 조회
     *
     * @param boardId
     * @return List<CommentListResponse>
     */
    public List<CommentListResponse> getCommentListBy(Long boardId) {
        return commentDao.selectList(boardId);
    }


    /**
     * 회원 댓글 작성
     *
     * @param memberId
     * @param commentSaveRequest
     * @return Long
     */
    public Long writeComment(Long memberId, CommentSaveRequest commentSaveRequest) {

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
     *
     * @param guestSaveRequest
     * @param commentSaveRequest
     * @return Long
     * @throws NoSuchAlgorithmException
     */
    public Long writeComment(GuestSaveRequest guestSaveRequest, CommentSaveRequest commentSaveRequest) throws NoSuchAlgorithmException {

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
     *
     * @param commentId
     */
    public void delete(Long commentId) {

        commentDao.deleteByCommentId(commentId);
    }

    /**
     * 댓글 소유권 검증
     *
     * @param commentId
     * @param username
     * @param guestPasswordCheckRequest
     * @throws NoSuchAlgorithmException
     */
    public void checkOwner(Long commentId, String username, GuestPasswordCheckRequest guestPasswordCheckRequest) throws NoSuchAlgorithmException {

        if (isAdminComment(commentId)) {
            throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);

        } else if (isGuestComment(commentId)) {

            String guestPassword = guestPasswordCheckRequest.getGuestPassword();

            validateGuestPassword(guestPassword);
            checkGuestPassword(commentId, guestPassword);

        } else if (isMemberComment(commentId)) {

            if (username == null) {
                throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
            }

            Long targetMemberId = memberDao.selectMemberIdByUsername(username); //검증 대상 memberId

            if (targetMemberId == null) {
                throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
            }

            Long realMemberId = commentDao.selectMemberId(commentId); //실제 memberId

            if (!targetMemberId.equals(realMemberId)) { //검증
                throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
            }
        }
    }

    private void checkGuestPassword(Long commentId, String guestPassword) throws NoSuchAlgorithmException {

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

    private void validateGuestPassword(String guestPassword) {

        if(guestPassword == null || guestPassword.equals("")) {
            throw new GuestPasswordInvalidException(ErrorCode.GUEST_PASSWORD_INVALID);
        }
    }

    private boolean isGuestComment(Long boardId) {
        return commentDao.selectGuestNickname(boardId) != null;
    }

    private boolean isMemberComment(Long boardId) {
        return commentDao.selectMemberId(boardId) != null;
    }

    private boolean isAdminComment(Long boardId) {
        return commentDao.selectAdminId(boardId) != null;
    }
}