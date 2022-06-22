package com.cgkim.myboardadmin.service.impl;

import com.cgkim.myboardadmin.dao.FAQDao;
import com.cgkim.myboardadmin.service.FAQService;
import com.cgkim.myboardadmin.vo.faq.FAQListResponse;
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
