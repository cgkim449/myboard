package com.cgkim.myboard.vo.board;

import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class BoardDetailResponse {
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private String boardWriter;
    private int boardViewCount;
    private boolean boardHasAttach;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date boardRegisterDate;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date boardUpdateDate;

    private Integer categoryId;
    private String categoryName;

    private List<CommentListResponse> commentList;
}
