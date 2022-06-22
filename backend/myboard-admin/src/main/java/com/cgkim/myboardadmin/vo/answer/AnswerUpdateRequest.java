package com.cgkim.myboardadmin.vo.answer;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class AnswerUpdateRequest {

    private String title;
    private String content;

    private Long answerId;
}
