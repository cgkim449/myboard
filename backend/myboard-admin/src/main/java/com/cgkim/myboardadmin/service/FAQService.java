package com.cgkim.myboardadmin.service;


import com.cgkim.myboardadmin.vo.faq.FAQListResponse;

import java.util.List;

public interface FAQService {
    List<FAQListResponse> getList(Integer categoryId);

    int getTotalCounts(Integer categoryId);
}
