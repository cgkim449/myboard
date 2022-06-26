package com.cgkim.myboard.controller;

import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.FAQService;
import com.cgkim.myboard.vo.faq.FAQListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * FAQ 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/faqs")
public class FAQController {

    private final FAQService faqService;

    /**
     * FAQ 목록 조회
     *
     * @param categoryId
     * @return ResponseEntity<SuccessResponse>
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getList(Integer categoryId) {

        List<FAQListResponse> faqList = faqService.getList(categoryId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("faqList", faqList));
    }
}