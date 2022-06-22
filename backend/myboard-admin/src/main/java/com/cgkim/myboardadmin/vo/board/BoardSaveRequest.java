package com.cgkim.myboardadmin.vo.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSaveRequest {
    private Integer categoryId;
    private String title;
    private String content;

    private Long boardId;
}
