package com.cgkim.myboardadmin.service;


import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.faq.FAQListResponse;
import com.cgkim.myboardadmin.vo.faq.FAQSaveRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FAQService {
    List<FAQListResponse> getList(Integer categoryId);

    @Transactional(rollbackFor = Exception.class)
    long write(String username, FAQSaveRequest faqSaveRequest, List<AttachVo> attachInsertList);
}
