package com.cgkim.myboard.vo.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardVo {
    //TODO: boardId만 setter
    private Integer categoryId;
    private String boardTitle;
    private String boardContent;

    private Long boardId;

    private Long memberId;

    private String guestNickname;
    private String guestPassword;
    private String guestPasswordConfirm;

    @Builder
    public BoardVo(
            Integer categoryId,
            String boardTitle,
            String boardContent,
            Long boardId,
            Long memberId,
            String guestNickname,
            String guestPassword
    ) {
        this.categoryId = categoryId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardId = boardId;
        this.memberId = memberId;
        this.guestNickname = guestNickname;
        this.guestPassword = guestPassword;
    }
}
