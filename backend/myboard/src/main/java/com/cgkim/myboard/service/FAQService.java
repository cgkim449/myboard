package com.cgkim.myboard.service;

import com.cgkim.myboard.dao.FAQDao;
import com.cgkim.myboard.vo.faq.FAQListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * FAQ Service
 */
@RequiredArgsConstructor
@Service
public class FAQService {
    private final FAQDao faqDao;

    /**
     * FAQ 리스트 조회
     *
     * @param categoryId
     * @return List<FAQListResponse>
     */
    public List<FAQListResponse> getList(Integer categoryId) {
        List<FAQListResponse> faqList = faqDao.selectList(categoryId);
        return faqList;
    }
}
