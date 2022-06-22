package com.cgkim.myboardadmin.vo.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardVo {
    //TODO: boardId만 setter
    private Integer categoryId;
    private String title;
    private String content;

    private Long boardId;

    private Long memberId;
    private Long adminId;

    private String guestNickname;
    private String guestPassword;
    private String guestPasswordConfirm;

    @Builder
    public BoardVo(
            Integer categoryId,
            String title,
            String content,
            Long boardId,
            Long memberId,
            Long adminId,
            String guestNickname,
            String guestPassword
    ) {
        this.categoryId = categoryId;
        this.title = title;
        this.content = content;
        this.boardId = boardId;
        this.memberId = memberId;
        this.adminId = adminId;
        this.guestNickname = guestNickname;
        this.guestPassword = guestPassword;
    }
}
