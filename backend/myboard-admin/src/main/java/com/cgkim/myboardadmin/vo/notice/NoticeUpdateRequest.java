package com.cgkim.myboardadmin.vo.notice;

import lombok.Getter;
import lombok.Setter;

/**
 * 공지 수정 요청
 */
@Getter
@Setter
public class NoticeUpdateRequest {

    private String title;

    private String content;

    private Long noticeId;
}
