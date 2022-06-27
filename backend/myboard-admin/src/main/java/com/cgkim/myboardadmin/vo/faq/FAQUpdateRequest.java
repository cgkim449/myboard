package com.cgkim.myboardadmin.vo.faq;

import lombok.Getter;
import lombok.Setter;


/**
 * FAQ 수정 요청
 */
@Getter
@Setter
public class FAQUpdateRequest {

    private String title;

    private String content;

    private Long faqId;
}
