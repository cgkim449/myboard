package com.cgkim.myboardadmin.vo.notice;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeVo {
    private String title;
    private String content;

    private Long noticeId;

    private Long adminId;

    @Builder
    public NoticeVo(String title, String content, Long noticeId, Long adminId) {
        this.title = title;
        this.content = content;
        this.noticeId = noticeId;
        this.adminId = adminId;
    }
}
