package com.cgkim.myboard.controller;

import com.cgkim.myboard.argumentresolver.LoginUser;
import com.cgkim.myboard.exception.LoginRequiredException;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.CommentService;
import com.cgkim.myboard.service.MemberService;
import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import com.cgkim.myboard.vo.common.GuestPasswordCheckRequest;
import com.cgkim.myboard.vo.common.GuestSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 자유게시판 댓글 컨트롤러
 */
@RequiredArgsConstructor
@RequestMapping("/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    private final MemberService memberService;

    /**
     * 댓글 목록 조회
     *
     * @param boardId
     * @return ResponseEntity<SuccessResponse>
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getCommentList(Long boardId) {

        List<CommentListResponse> commentList = commentService.getCommentListBy(boardId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("commentList", commentList));
    }

    /**
     * 회원 댓글 작성
     *
     * @param username
     * @param commentSaveRequest
     * @return ResponseEntity<SuccessResponse>
     */
    @PostMapping("/member")
    public ResponseEntity<SuccessResponse> writeMemberComment(@LoginUser String username,
                                                              @Valid CommentSaveRequest commentSaveRequest
    ) {

        if (username == null) {
            throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
        }

        Long memberId = memberService.getMemberIdBy(username);

        Long commentId = commentService.writeComment(memberId, commentSaveRequest);

        return ResponseEntity.created(URI.create("/comments/" + commentId)).body(new SuccessResponse());
    }

    /**
     * 익명 댓글 작성
     *
     * @param guestSaveRequest
     * @param commentSaveRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/guest")
    public ResponseEntity<SuccessResponse> writeGuestComment(@Valid GuestSaveRequest guestSaveRequest,
                                                             @Valid CommentSaveRequest commentSaveRequest
    ) throws NoSuchAlgorithmException {

        long commentId = commentService.writeComment(guestSaveRequest, commentSaveRequest);

        return ResponseEntity.created(URI.create("/comments/" + commentId)).body(new SuccessResponse());
    }

    /**
     * 댓글 삭제
     *
     * @param username
     * @param commentId
     * @param guestPasswordCheckRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> deleteComment(@PathVariable Long commentId,
                                                         @LoginUser String username,
                                                         @RequestBody(required = false) GuestPasswordCheckRequest guestPasswordCheckRequest
    ) throws NoSuchAlgorithmException {

        commentService.checkOwner(commentId, username, guestPasswordCheckRequest);
        commentService.delete(commentId);

        return ResponseEntity.noContent().build();
    }
}
