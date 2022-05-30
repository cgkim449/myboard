package com.cgkim.myboard.vo.board;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardPwCheckRequest {
    Long boardId;
    String guestPassword;

    @Builder
    public BoardPwCheckRequest(Long boardId, String guestPassword) {
        this.boardId = boardId;
        this.guestPassword = guestPassword;
    }
}
