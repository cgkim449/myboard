package com.cgkim.myboard.vo.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BoardListResponse {
    private Long boardId;
    private String boardTitle;
    private String boardWriter;
    private int boardViewCount;
    private int boardHasAttach;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date boardRegisterDate;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date boardUpdateDate;

    private Integer categoryId;
    private String categoryName;
}
