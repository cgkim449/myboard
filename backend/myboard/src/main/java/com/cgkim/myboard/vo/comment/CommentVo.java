package com.cgkim.myboard.vo.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentVo {
    private Long boardId;
    private String commentContent;

    private String guestNickname;
    private String guestPassword;

    private Long userId;

    @Builder
    public CommentVo(Long boardId, String commentContent, String guestNickname, String guestPassword, Long userId) {
        this.boardId = boardId;
        this.commentContent = commentContent;
        this.guestNickname = guestNickname;
        this.guestPassword = guestPassword;
        this.userId = userId;
    }
}
