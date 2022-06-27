package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.exception.AttachNotFoundException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.service.QuestionAttachService;
import com.cgkim.myboardadmin.util.AttachDownloadResponseBuilder;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * Q&A 질문 첨부파일 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/question-attaches")
public class QuestionAttachController {

    private final QuestionAttachService attachService;

    private final AttachDownloadResponseBuilder attachDownloadResponseBuilder;

    /**
     * 질문 첨부파일 다운로드
     *
     * @param attachId
     * @return ResponseEntity<Resource>
     */
    @GetMapping("/{attachId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long attachId) {

        AttachVo attach = attachService.getAttachBy(attachId);

        return attachDownloadResponseBuilder.buildResponseWith(attach);
    }
}
