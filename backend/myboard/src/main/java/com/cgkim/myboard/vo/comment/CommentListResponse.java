package com.cgkim.myboard.vo.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentListResponse {
    private Long commentId;
    private Long boardId;
    private String content;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private Date registerDate;

    private String guestNickname;
    //TODO: 변수명 수정 memberNickname
    private String nickname;
    private String adminNickname;
}
