package com.cgkim.myboardadmin.vo.notice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 공지 목록 응답
 */
@Getter
@Setter
public class NoticeListResponse {

    private Long noticeId;

    private String title;

    private int hasAttach;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registerDate;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    private String adminNickname;

    private String thumbnailUri;
}
