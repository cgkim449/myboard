package com.cgkim.myboardadmin.vo.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class NoticeUpdateRequest {

    private String title;
    private String content;

    private Long noticeId;
}
