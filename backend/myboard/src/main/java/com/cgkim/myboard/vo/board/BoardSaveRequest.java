package com.cgkim.myboard.vo.board;

import lombok.Getter;
import lombok.Setter;

/**
 * 게시물 등록 요청
 */
@Getter
@Setter
public class BoardSaveRequest {

    private Integer categoryId;

    private String title;

    private String content;

    private Long boardId;
}
