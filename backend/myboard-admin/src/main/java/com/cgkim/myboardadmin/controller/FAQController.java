package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.response.SuccessResponse;
import com.cgkim.myboardadmin.service.FAQService;
import com.cgkim.myboardadmin.vo.faq.FAQListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/faqs")
public class FAQController {

    private final FAQService faqService;

    @GetMapping
    public ResponseEntity<SuccessResponse> getList(Integer categoryId){

        List<FAQListResponse> faqList = faqService.getList(categoryId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("faqList", faqList));
    }
}