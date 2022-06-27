package com.cgkim.myboardadmin.vo.faq;

import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 게시물 상세 조회 응답
 */
@Getter
@Setter
public class FAQDetailResponse {

    private Long faqId;

    private String title;

    private String content;

    private boolean hasAttach;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registerDate;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    private Integer categoryId;

    private String categoryName;

    private String adminUsername;

    private String adminNickname;

    private List<AttachVo> attachList;
}
