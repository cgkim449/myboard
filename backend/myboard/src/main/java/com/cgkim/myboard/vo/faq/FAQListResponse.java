package com.cgkim.myboard.vo.faq;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * FAQ 목록 응답
 */
@Getter
@Setter
public class FAQListResponse {

    private Long faqId;

    private Integer categoryId;

    private String title;

    private String content;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registerDate;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    private String categoryName;
}
