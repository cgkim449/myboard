package com.cgkim.myboard.vo.board;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class BoardUpdateRequest {

    private String guestName;
    private String boardTitle;
    private String boardContent;

    private Long boardId;
    private String guestPassword;
}
