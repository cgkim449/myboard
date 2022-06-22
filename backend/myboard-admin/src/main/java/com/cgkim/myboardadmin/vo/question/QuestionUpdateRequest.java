package com.cgkim.myboardadmin.vo.question;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class QuestionUpdateRequest {

    private String title;
    private String content;
    private Integer isSecret;

    private Long questionId;
}
