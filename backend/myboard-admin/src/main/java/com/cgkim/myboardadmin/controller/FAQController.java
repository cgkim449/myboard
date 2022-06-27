package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.argumentresolver.LoginUser;
import com.cgkim.myboardadmin.response.SuccessResponse;
import com.cgkim.myboardadmin.service.FAQAttachService;
import com.cgkim.myboardadmin.service.FAQService;
import com.cgkim.myboardadmin.util.FileHandler;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.board.BoardUpdateRequest;
import com.cgkim.myboardadmin.vo.common.FileSaveRequest;
import com.cgkim.myboardadmin.vo.faq.FAQDetailResponse;
import com.cgkim.myboardadmin.vo.faq.FAQListResponse;
import com.cgkim.myboardadmin.vo.faq.FAQSaveRequest;
import com.cgkim.myboardadmin.vo.faq.FAQUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * FAQ 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/faqs")
public class FAQController {

    private final FAQService faqService;

    private final FileHandler fileHandler;

    private final FAQAttachService attachService;

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

    /**
     * FAQ 상세 조회
     *
     * @param faqId
     * @return ResponseEntity<SuccessResponse>
     */
    @GetMapping("/{faqId}")
    public ResponseEntity<SuccessResponse> getDetail(@PathVariable Long faqId) {

        FAQDetailResponse faqDetail = faqService.viewFAQDetail(faqId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("faqDetail", faqDetail));
    }

    /**
     * FAQ 작성
     *
     * @param username
     * @param faqSaveRequest
     * @param fileSaveRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> write(@LoginUser String username,
                                                 @Valid FAQSaveRequest faqSaveRequest,
                                                 @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {

        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //첨부파일 생성 (C://upload)
        Long faqId = faqService.write(username, faqSaveRequest, attachInsertList); //글 작성

        return ResponseEntity.created(URI.create("/admin/faqs/" + faqId)).body(new SuccessResponse());
    }

    /**
     * FAQ 삭제
     *
     * @param faqId
     * @return ResponseEntity<SuccessResponse>
     */
    @DeleteMapping("/{faqId}")
    public ResponseEntity<SuccessResponse> deleteFAQ(@PathVariable Long faqId) {

        List<AttachVo> attachDeleteList = attachService.getList(faqId);

        faqService.delete(faqId);
        fileHandler.deleteFiles(attachDeleteList);

        return ResponseEntity.noContent().build();
    }

    /**
     * FAQ 수정
     *
     * @param faqId
     * @param faqUpdateRequest
     * @param fileSaveRequest
     * @param attachDeleteRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException
     */
    @PatchMapping("/{faqId}")
    public ResponseEntity<SuccessResponse> update(@PathVariable Long faqId,
                                                  @Valid FAQUpdateRequest faqUpdateRequest,
                                                  @Valid FileSaveRequest fileSaveRequest,
                                                  Long[] attachDeleteRequest
    ) throws IOException {

        List<AttachVo> attachDeleteList = attachService.getList(attachDeleteRequest);
        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());

        faqService.modify(faqId, faqUpdateRequest, attachInsertList, attachDeleteList);

        fileHandler.deleteFiles(attachDeleteList);

        return ResponseEntity.ok(new SuccessResponse());
    }
}