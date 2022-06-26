package com.cgkim.myboardadmin.vo.faq;

import lombok.Getter;
import lombok.Setter;

/**
 * FAQ 등록 요청
 */
@Getter
@Setter
public class FAQSaveRequest {

    private Integer categoryId;

    private String title;

    private String content;

    private Long faqId;
}
