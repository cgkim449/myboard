package com.cgkim.myboardadmin.vo.answer;

import lombok.Getter;
import lombok.Setter;

/**
 * 답변 등록 요청
 */
@Getter
@Setter
public class AnswerSaveRequest {

    private String title;

    private String content;

    private Long questionId;
}
