package com.cgkim.myboard.controller;

import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.exception.GuestPasswordInvalidException;
import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.CommentService;
import com.cgkim.myboard.service.MemberService;
import com.cgkim.myboard.validation.CommentSaveRequestValidator;
import com.cgkim.myboard.validation.GuestSaveRequestValidator;
import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import com.cgkim.myboard.vo.member.GuestPasswordCheckRequest;
import com.cgkim.myboard.vo.member.GuestSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@RequiredArgsConstructor
@RequestMapping("/comments")
@RestController
public class CommentController {
    @Setter
    private String username;
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
     * 댓글 리스트 API
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getCommentList(Long boardId) {
        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("commentList", commentService.getCommentList(boardId)));
    }

    /**
     * 댓글 작성 API
     */
    @PostMapping("/member")
    public ResponseEntity<SuccessResponse> writeMemberComment(
            @Valid CommentSaveRequest commentSaveRequest
    ) {
        Long memberId = memberService.getMemberId(username);

        long commentId = commentService.writeComment(memberId, commentSaveRequest);
        return ResponseEntity.created(URI.create("/comments/" + commentId)).body(new SuccessResponse());
    }

    /**
     * 댓글 작성 API
     */
    @PostMapping("/guest")
    public ResponseEntity<SuccessResponse> writeGuestComment(
            @Valid GuestSaveRequest guestSaveRequest,
            @Valid CommentSaveRequest commentSaveRequest
    ) throws NoSuchAlgorithmException {
        long commentId =  commentService.writeComment(guestSaveRequest, commentSaveRequest);
        return ResponseEntity.created(URI.create("/comments/" + commentId)).body(new SuccessResponse());
    }


    /**
     * 댓글 삭제 API
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> deleteComment(
            @PathVariable Long commentId,
            @RequestBody GuestPasswordCheckRequest guestPasswordCheckRequest
    ) throws NoSuchAlgorithmException {
        if(commentService.isAnonymous(commentId)) { //익명 댓글일때만
            validateGuestPassword(guestPasswordCheckRequest.getGuestPassword()); //비밀번호 유효성 검증
            commentService.checkGuestPassword(commentId, guestPasswordCheckRequest.getGuestPassword()); //비밀번호 체크
        }
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }

    private void validateGuestPassword(String guestPassword) {
        if(guestPassword == null || guestPassword.equals("")) {
            throw new GuestPasswordInvalidException(ErrorCode.GUEST_PASSWORD_INVALID);
        }
    }
}
