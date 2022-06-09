package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.FAQDao;
import com.cgkim.myboard.service.FAQService;
import com.cgkim.myboard.vo.faq.FAQListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FAQServiceImpl implements FAQService {
    private final FAQDao faqDao;
    @Override
    public List<FAQListResponse> getList(Integer categoryId) {
        List<FAQListResponse> faqList = faqDao.selectList(categoryId);
        return faqList;
    }

    @Override
    public int getTotalCounts(Integer categoryId) {
        return 0;
    }
}
