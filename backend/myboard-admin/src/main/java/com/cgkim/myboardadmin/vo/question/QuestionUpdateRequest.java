package com.cgkim.myboardadmin.vo.question;

import lombok.Getter;
import lombok.Setter;

/**
 * 질문 수정 요청
 */
@Getter
@Setter
public class QuestionUpdateRequest {

    private String title;

    private String content;

    private Integer isSecret;

    private Long questionId;
}
