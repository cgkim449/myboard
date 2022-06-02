package com.cgkim.myboard.controller;

import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.CommentService;
import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/comments")
@RestController
public class CommentController {
    final CommentService commentService;

    /**
     * 특정 게시물의 댓글 리스트
     *
     * @param boardId
     * @return
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
     *
     * @param commentSaveRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> write(
            @RequestBody CommentSaveRequest commentSaveRequest
    ) {
        commentService.writeComment(commentSaveRequest);

        return ResponseEntity
                .ok()
                .body(new SuccessResponse());
    }
}
