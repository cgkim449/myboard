package com.cgkim.myboard.vo.question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionSaveRequest {
    private Integer categoryId;
    private String title;
    private String content;
    private Integer isSecret;
    private Long questionId;

}
