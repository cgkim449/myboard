package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.service.NoticeAttachService;
import com.cgkim.myboardadmin.util.AttachDownloadResponseBuilder;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 공지사항 첨부파일 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/notice-attaches")
public class NoticeAttachController {

    private final NoticeAttachService attachService;

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
