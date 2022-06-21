package com.cgkim.myboard.controller;

import com.cgkim.myboard.argumentresolver.LoginUser;
import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.exception.LoginRequiredException;
import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.CommentService;
import com.cgkim.myboard.service.MemberService;
import com.cgkim.myboard.validator.CommentSaveRequestValidator;
import com.cgkim.myboard.validator.GuestSaveRequestValidator;
import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import com.cgkim.myboard.vo.member.GuestPasswordCheckRequest;
import com.cgkim.myboard.vo.member.GuestSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/comments")
@RestController
public class CommentController {
    private final CommentService commentService;
    private final MemberService memberService;
    private final CommentSaveRequestValidator commentSaveRequestValidator;
    private final GuestSaveRequestValidator guestSaveRequestValidator;//TODO: 전역등록

    /**
     * Validator 등록
     */
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        addValidators(webDataBinder);
    }

    /**
     * Validator 등록
     */
    private void addValidators(WebDataBinder webDataBinder) {

        if (webDataBinder.getTarget() == null) {
            return;
        }

        final List<Validator> validatorList = List.of(
                commentSaveRequestValidator,
                guestSaveRequestValidator
        );

        for (Validator validator : validatorList) {
            if (validator.supports(webDataBinder.getTarget().getClass())) {
                webDataBinder.addValidators(validator);
            }
        }
    }

    /**
     * 댓글 목록 조회
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getCommentList(Long boardId) {
        List<CommentListResponse> commentList = commentService.getCommentList(boardId);
        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("commentList", commentList));
    }

    /**
     * 회원 댓글 작성
     */
    @PostMapping("/member")
    public ResponseEntity<SuccessResponse> writeMemberComment(@LoginUser String username,
                                                              @Valid CommentSaveRequest commentSaveRequest
    ) {

        if(username == null) {
            throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
        }

        long commentId = commentService.writeComment(username, commentSaveRequest);

        return ResponseEntity.created(URI.create("/comments/" + commentId)).body(new SuccessResponse());
    }

    /**
     * 익명 댓글 작성
     */
    @PostMapping("/guest")
    public ResponseEntity<SuccessResponse> writeGuestComment(@Valid GuestSaveRequest guestSaveRequest,
                                                             @Valid CommentSaveRequest commentSaveRequest
    ) throws NoSuchAlgorithmException {

        long commentId =  commentService.writeComment(guestSaveRequest, commentSaveRequest);

        return ResponseEntity.created(URI.create("/comments/" + commentId)).body(new SuccessResponse());
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> deleteComment(@LoginUser String username,
                                                         @PathVariable Long commentId,
                                                         @RequestBody(required = false) GuestPasswordCheckRequest guestPasswordCheckRequest
    ) throws NoSuchAlgorithmException {
        //TODO: 리팩토링
        String guestPassword = null;
        if(guestPasswordCheckRequest != null) {
            guestPassword = guestPasswordCheckRequest.getGuestPassword();
        }

        commentService.checkOwner(commentId, username, guestPassword);
        commentService.delete(commentId);

        return ResponseEntity.noContent().build();
    }
}
