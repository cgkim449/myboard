package com.cgkim.myboardadmin.vo.answer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class AnswerVo {
    private Long answerId;
    private Long adminId;
    private Long questionId;
    private String title;
    private String content;

    private String adminUsername;
    private String adminNickname;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registerDate;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    @Builder
    public AnswerVo(Long answerId, Long adminId, Long questionId, String title, String content) {
        this.answerId = answerId;
        this.adminId = adminId;
        this.questionId = questionId;
        this.title = title;
        this.content = content;
    }
}
