package com.cgkim.myboard.controller;

import com.cgkim.myboard.argumentresolver.Guest;
import com.cgkim.myboard.argumentresolver.LoginUser;
import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.CommentService;
import com.cgkim.myboard.validation.CommentSaveRequestValidator;
import com.cgkim.myboard.validation.GuestSaveRequestValidator;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.attach.FileSaveRequest;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import com.cgkim.myboard.vo.user.GuestSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/comments")
@RestController
public class CommentController {
    private final CommentService commentService;
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
     * 특정 게시물의 댓글 리스트
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getCommentList(Long boardId) {
        return ResponseEntity
                .ok()
                .body(new SuccessResponse()
                        .put("commentList", commentService.getCommentList(boardId)));
    }

    /**
     * 댓글 작성
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> write(
            @LoginUser Long userId,
            @Guest GuestSaveRequest guestSaveRequest,
            @Valid CommentSaveRequest commentSaveRequest
    ) {
        if(isLogin(userId)) { //회원 댓글작성
            commentService.writeComment(userId, commentSaveRequest);
        } else { //익명 댓글작성
            commentService.writeComment(guestSaveRequest, commentSaveRequest);
        }
        return ResponseEntity
                .ok()
                .body(new SuccessResponse());
    }

    private boolean isLogin(Long userId) {
        return userId != null;
    }
}
