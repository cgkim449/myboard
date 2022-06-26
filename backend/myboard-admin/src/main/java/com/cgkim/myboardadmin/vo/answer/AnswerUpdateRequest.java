package com.cgkim.myboardadmin.vo.answer;

import lombok.Getter;
import lombok.Setter;

/**
 * 답변 수정 요청
 */
@Getter
@Setter
public class AnswerUpdateRequest {

    private String title;

    private String content;

    private Long answerId;
}
