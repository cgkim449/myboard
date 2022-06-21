package com.cgkim.myboard.vo.board;

import com.cgkim.myboard.vo.attach.AttachVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BoardListResponse {
    private Long boardId;
    private String title;
    private int viewCount;
    private int hasAttach;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registerDate;
    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    private Integer categoryId;
    private String categoryName;

    private String guestNickname;

    private String memberNickname;
    private String adminNickname;

    private String thumbnailUri;
}
