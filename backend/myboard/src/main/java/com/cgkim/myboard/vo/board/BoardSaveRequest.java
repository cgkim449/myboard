package com.cgkim.myboard.vo.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSaveRequest {
    private Integer categoryId;
    private String boardTitle;
    private String boardContent;

    private Long boardId;
}
