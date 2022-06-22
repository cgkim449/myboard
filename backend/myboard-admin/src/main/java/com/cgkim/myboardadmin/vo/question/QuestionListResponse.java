package com.cgkim.myboardadmin.vo.question;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class QuestionListResponse {
    private Long questionId;
    private Long answerId;
    private String title;
    private int viewCount;
    private int hasAttach;
    private int isSecret;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registerDate;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    private Integer categoryId;
    private String categoryName;

    private String memberNickname;
    private String adminNickname;

    private String thumbnailUri;
}
