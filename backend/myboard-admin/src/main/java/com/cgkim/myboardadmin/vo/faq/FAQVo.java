package com.cgkim.myboardadmin.vo.faq;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FAQVo {
    private Integer categoryId;
    private String title;
    private String content;

    private Long faqId;

    private Long adminId;


    @Builder
    public FAQVo(
            Integer categoryId,
            String title,
            String content,
            Long faqId,
            Long adminId
    ) {
        this.categoryId = categoryId;
        this.title = title;
        this.content = content;
        this.faqId = faqId;
        this.adminId = adminId;
    }
}
