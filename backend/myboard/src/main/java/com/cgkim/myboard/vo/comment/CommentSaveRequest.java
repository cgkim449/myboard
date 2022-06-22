package com.cgkim.myboard.vo.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentSaveRequest {
    private Long boardId;
    private String content;

}
