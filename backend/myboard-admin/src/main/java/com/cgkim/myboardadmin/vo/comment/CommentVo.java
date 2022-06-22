package com.cgkim.myboardadmin.vo.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentVo {
    private Long commentId;
    private Long boardId;
    private String content;

    private String guestNickname;
    private String guestPassword;

    private Long memberId;
    private Long adminId;

    @Builder
    public CommentVo(
            Long commentId,
            Long boardId,
            String content,
            String guestNickname,
            String guestPassword,
            Long memberId,
            Long adminId
    ) {
        this.commentId = commentId;
        this.boardId = boardId;
        this.content = content;
        this.guestNickname = guestNickname;
        this.guestPassword = guestPassword;
        this.memberId = memberId;
        this.adminId = adminId;
    }
}
