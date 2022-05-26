package com.cgkim.myboard.controller;

import com.cgkim.myboard.service.CommentService;
import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/comments")
@RestController
public class CommentController {
    final CommentService commentService;

    @GetMapping
    public ResponseEntity<?> getCommentList(Long boardId) {
        List<CommentListResponse> commentList =  commentService.getCommentList(boardId);

        return ResponseEntity.ok().body(Map.of("commentList", commentList));
    }

    @PostMapping
    public ResponseEntity<Void> write(@RequestBody CommentSaveRequest commentSaveRequest) {
        commentService.writeComment(commentSaveRequest);

        return ResponseEntity.ok().build();
    }
}
