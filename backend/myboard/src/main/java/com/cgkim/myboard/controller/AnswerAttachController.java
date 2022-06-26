package com.cgkim.myboard.controller;

import com.cgkim.myboard.service.AnswerAttachService;
import com.cgkim.myboard.util.AttachDownloadResponseBuilder;
import com.cgkim.myboard.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Q&A 답변 첨부파일 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/answer-attaches")
public class AnswerAttachController {

    private final AnswerAttachService attachService;

    private final AttachDownloadResponseBuilder attachDownloadResponseBuilder;

    /**
     * 첨부파일 다운로드
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
