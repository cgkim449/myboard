package com.cgkim.myboardadmin.vo.board;

import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.comment.CommentListResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 게시물 상세 조회 응답
 */
@Getter
@Setter
public class BoardDetailResponse {

    private Long boardId;

    private String title;

    private String content;

    private int viewCount;

    private boolean hasAttach;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registerDate;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    private Integer categoryId;

    private String categoryName;

    private String guestNickname;

    private String memberUsername;

    private String memberNickname;

    private String adminUsername;

    private String adminNickname;

    private List<CommentListResponse> commentList;

    private List<AttachVo> attachList;
}
