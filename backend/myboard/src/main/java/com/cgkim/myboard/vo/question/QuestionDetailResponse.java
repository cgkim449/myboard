package com.cgkim.myboard.vo.question;

import com.cgkim.myboard.vo.answer.AnswerVo;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class QuestionDetailResponse {
    private Long questionId;
    private String title;
    private String content;
    private int viewCount;
    private boolean hasAttach;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registerDate;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    private Integer categoryId;
    private String categoryName;

    private List<AttachVo> attachList;

    private String username; // 로그인 사용자 이메일
    private String nickname; // 로그인 사용자 닉네임

    private AnswerVo answer;

}
