package com.cgkim.myboardadmin.vo.faq;

import lombok.Getter;

import java.util.Date;

//TODO: searchRequest 중복됨
@Getter
public class FAQSearchRequest {
    private Integer page; // 페이지
    private String keyword; // 검색어
    private Integer categoryId; // 카테고리
    private Date fromDate; // 검색 시작 날짜
    private Date toDate;  // 검색 끝 날짜

    public FAQSearchRequest(Integer page, String keyword, Integer categoryId, Date fromDate, Date toDate) {
        this.page = (page == null || page < 1) ? 1 : page;
        this.keyword = keyword;
        this.categoryId = categoryId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public final int limit = 10; // 한 페이지에 표시할 게시물 수

    public int getOffset() {
        return (page - 1) * limit;
    }
}