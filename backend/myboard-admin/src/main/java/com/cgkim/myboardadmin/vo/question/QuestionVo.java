package com.cgkim.myboardadmin.vo.question;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 질문 VO
 */
@Getter
@Setter
public class QuestionVo {

    private Integer categoryId;

    private String title;

    private String content;

    private Long questionId;

    private Integer isSecret;

    private Long memberId;

    private Long adminId;

    /**
     * 주입
     *
     * @param categoryId
     * @param title
     * @param content
     * @param questionId
     * @param isSecret
     * @param memberId
     * @param adminId
     */
    @Builder
    public QuestionVo(Integer categoryId, String title, String content, Long questionId, Integer isSecret, Long memberId, Long adminId) {
        this.categoryId = categoryId;
        this.title = title;
        this.content = content;
        this.questionId = questionId;
        this.isSecret = isSecret;
        this.memberId = memberId;
        this.adminId = adminId;
    }
}
