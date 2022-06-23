package com.cgkim.myboardadmin.vo.notice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeSaveRequest {
    private String title;
    private String content;

    private Long noticeId;
}
