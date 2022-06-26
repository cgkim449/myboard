package com.cgkim.myboardadmin.vo.question;

import com.cgkim.myboardadmin.vo.answer.AnswerDetailResponse;
import com.cgkim.myboardadmin.vo.answer.AnswerVo;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 질문 상세 조회 응답
 */
@Getter
@Setter
public class QuestionDetailResponse {

    private Long questionId;

    private String title;

    private String content;

    private int viewCount;

    private boolean hasAttach;

    private Integer isSecret;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registerDate;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    private Integer categoryId;

    private String categoryName;

    private String memberUsername;

    private String memberNickname;

    private String adminUsername;

    private String adminNickname;

    private AnswerDetailResponse answer;

    private List<AttachVo> attachList;
}
