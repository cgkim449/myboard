package com.cgkim.myboard.vo.answer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerSaveRequest {
    private String title;
    private String content;
    private Long questionId;
}
