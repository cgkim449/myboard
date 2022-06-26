package com.cgkim.myboard.vo.notice;

import lombok.Getter;

import java.util.Date;

/**
 * 공지 검색 요청
 */
@Getter
public class NoticeSearchRequest {

    private final Integer page; // 페이지

    private final String keyword; // 검색어

    private final Integer categoryId; // 카테고리

    private final Date fromDate; // 검색 시작 날짜

    private final Date toDate;  // 검색 끝 날짜

    /**
     * 주입
     *
     * @param page
     * @param keyword
     * @param categoryId
     * @param fromDate
     * @param toDate
     */
    public NoticeSearchRequest(Integer page, String keyword, Integer categoryId, Date fromDate, Date toDate) {
        this.page = (page == null || page < 1) ? 1 : page;
        this.keyword = keyword;
        this.categoryId = categoryId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public final int limit = 12; // 한 페이지에 표시할 게시물 수

    /**
     * 페이지 offset
     *
     * @return int
     */
    public int getOffset() {
        return (page - 1) * limit;
    }
}
