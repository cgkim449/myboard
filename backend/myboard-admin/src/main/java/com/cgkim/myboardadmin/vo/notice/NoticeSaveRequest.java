package com.cgkim.myboardadmin.vo.notice;

import lombok.Getter;
import lombok.Setter;

/**
 * 공지 등록 요청
 */
@Getter
@Setter
public class NoticeSaveRequest {

    private String title;

    private String content;

    private Long noticeId;
}
