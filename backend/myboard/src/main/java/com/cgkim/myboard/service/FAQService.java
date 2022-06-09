package com.cgkim.myboard.service;

import com.cgkim.myboard.vo.faq.FAQListResponse;

import java.util.List;

public interface FAQService {
    List<FAQListResponse> getList(Integer categoryId);

    int getTotalCounts(Integer categoryId);
}
