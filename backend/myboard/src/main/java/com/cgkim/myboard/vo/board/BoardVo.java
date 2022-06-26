package com.cgkim.myboard.vo.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시물 VO
 */
@Getter
@Setter
public class BoardVo {

    private Integer categoryId;

    private String title;

    private String content;

    private Long boardId;

    private Long memberId;

    private Long adminId;

    private String guestNickname;

    private String guestPassword;

    private String guestPasswordConfirm;

    /**
     * 주입
     *
     * @param categoryId
     * @param title
     * @param content
     * @param boardId
     * @param memberId
     * @param adminId
     * @param guestNickname
     * @param guestPassword
     */
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
