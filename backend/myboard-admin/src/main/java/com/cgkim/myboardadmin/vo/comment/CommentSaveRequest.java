package com.cgkim.myboardadmin.vo.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 댓글 등록 요청
 */
@AllArgsConstructor
@Getter
public class CommentSaveRequest {

    private Long boardId;

    private String content;
}
