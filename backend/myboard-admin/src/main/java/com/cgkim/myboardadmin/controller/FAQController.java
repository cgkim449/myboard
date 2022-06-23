package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.argumentresolver.LoginUser;
import com.cgkim.myboardadmin.response.SuccessResponse;
import com.cgkim.myboardadmin.service.FAQService;
import com.cgkim.myboardadmin.util.FileHandler;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.attach.FileSaveRequest;
import com.cgkim.myboardadmin.vo.faq.FAQListResponse;
import com.cgkim.myboardadmin.vo.faq.FAQSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/faqs")
public class FAQController {

    private final FAQService faqService;
    private final FileHandler fileHandler;

    /**
     * FAQ 목록 조회
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getList(Integer categoryId){

        List<FAQListResponse> faqList = faqService.getList(categoryId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("faqList", faqList));
    }

    /**
     * FAQ 작성
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> write(@LoginUser String username,
                                                 @Valid FAQSaveRequest faqSaveRequest,
                                                 @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {

        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //첨부파일 생성 (C://upload)
        long faqId = faqService.write(username, faqSaveRequest, attachInsertList); //글 작성

        return ResponseEntity.created(URI.create("/admin/faqs/" + faqId)).body(new SuccessResponse());
    }
}