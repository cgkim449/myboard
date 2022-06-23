package com.cgkim.myboardadmin.vo.faq;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FAQSaveRequest {
    private Integer categoryId;
    private String title;
    private String content;

    private Long faqId;
}
