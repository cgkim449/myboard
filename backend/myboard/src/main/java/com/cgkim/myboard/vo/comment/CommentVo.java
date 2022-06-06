package com.cgkim.myboard.vo.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentVo {
    private Long commentId;
    private Long boardId;
    private String commentContent;

    private String guestNickname;
    private String guestPassword;

    private Long memberId;

    @Builder
    public CommentVo(Long commentId, Long boardId, String commentContent, String guestNickname, String guestPassword, Long memberId) {
        this.commentId = commentId;
        this.boardId = boardId;
        this.commentContent = commentContent;
        this.guestNickname = guestNickname;
        this.guestPassword = guestPassword;
        this.memberId = memberId;
    }
}
