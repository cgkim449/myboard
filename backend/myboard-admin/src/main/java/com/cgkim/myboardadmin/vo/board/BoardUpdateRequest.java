package com.cgkim.myboardadmin.vo.board;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class BoardUpdateRequest {

    private String title;
    private String content;

    private Long boardId;
}
