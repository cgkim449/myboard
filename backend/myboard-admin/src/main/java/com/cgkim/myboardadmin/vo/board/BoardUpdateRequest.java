package com.cgkim.myboardadmin.vo.board;

import lombok.Getter;
import lombok.Setter;


/**
 * 게시물 수정 요청
 */
@Getter
@Setter
public class BoardUpdateRequest {

    private String title;

    private String content;

    private Long boardId;
}
