package com.cgkim.myboard.vo.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BoardUpdateRequest {

    private String title;
    private String content;

    private Long boardId;
}
