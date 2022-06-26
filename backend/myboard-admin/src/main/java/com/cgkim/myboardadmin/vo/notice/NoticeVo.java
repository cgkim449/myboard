package com.cgkim.myboardadmin.vo.notice;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 공지 VO
 */
@Getter
@Setter
public class NoticeVo {

    private String title;

    private String content;

    private Long noticeId;

    private Long adminId;

    /**
     * 주입
     *
     * @param title
     * @param content
     * @param noticeId
     * @param adminId
     */
    @Builder
    public NoticeVo(String title, String content, Long noticeId, Long adminId) {
        this.title = title;
        this.content = content;
        this.noticeId = noticeId;
        this.adminId = adminId;
    }
}
