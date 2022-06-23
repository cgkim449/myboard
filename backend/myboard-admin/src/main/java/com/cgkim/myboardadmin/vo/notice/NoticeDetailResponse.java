package com.cgkim.myboardadmin.vo.notice;

import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class NoticeDetailResponse {
    private Long noticeId;
    private String title;
    private String content;
    private String adminNickname;
    private boolean hasAttach;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registerDate;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    private List<AttachVo> attachList;
}
