package com.cgkim.myboardadmin.vo.question;

import lombok.Getter;
import lombok.Setter;

/**
 * 질문 등록 요청
 */
@Getter
@Setter
public class QuestionSaveRequest {

    private Integer categoryId;

    private String title;

    private String content;

    private Integer isSecret;

    private Long questionId;

}
