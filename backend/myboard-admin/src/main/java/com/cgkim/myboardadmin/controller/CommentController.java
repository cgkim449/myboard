package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.argumentresolver.LoginUser;
import com.cgkim.myboardadmin.response.SuccessResponse;
import com.cgkim.myboardadmin.service.CommentService;
import com.cgkim.myboardadmin.vo.comment.CommentListResponse;
import com.cgkim.myboardadmin.vo.comment.CommentSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * 자유게시판 댓글 컨트롤러
 */
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 목록 조회
     *
     * @param boardId
     * @return ResponseEntity<SuccessResponse>
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getCommentList(Long boardId) {

        List<CommentListResponse> commentList = commentService.getCommentList(boardId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("commentList", commentList));
    }

    /**
     * 댓글 작성
     *
     * @param username
     * @param commentSaveRequest
     * @return ResponseEntity<SuccessResponse>
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> writeComment(@LoginUser String username,
                                                        @Valid CommentSaveRequest commentSaveRequest
    ) {

        Long commentId = commentService.writeComment(username, commentSaveRequest);

        return ResponseEntity.created(URI.create("/admin/comments/" + commentId)).body(new SuccessResponse());
    }

    /**
     * 댓글 삭제
     *
     * @param commentId
     * @return ResponseEntity<SuccessResponse>
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> deleteComment(@PathVariable Long commentId) {

        commentService.delete(commentId);

        return ResponseEntity.noContent().build();
    }
}
