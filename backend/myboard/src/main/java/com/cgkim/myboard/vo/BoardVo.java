package com.cgkim.myboard.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BoardVo {
    private Long boardId;
    private Integer categoryId;
    private String boardTitle;
    private String boardContent;
    private String boardWriter;
    private int boardViewCount;
    private String boardPassword;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date boardRegisterDate;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date boardUpdateDate;
}
