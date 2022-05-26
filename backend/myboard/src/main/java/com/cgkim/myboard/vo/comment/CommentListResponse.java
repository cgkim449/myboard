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
    private String commentNickname;
    private String commentContent;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private Date commentRegisterDate;
}
