package com.cgkim.myboard.vo.answer;

import com.cgkim.myboard.vo.attach.AttachVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AnswerDetailResponse {
    private Long answerId;
    private Long questionId;
    private String title;
    private String content;
    private boolean hasAttach;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registerDate;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    private String adminUsername; //관리자
    private String adminNickname; //관리자

    private List<AttachVo> attachList;
}
