package com.cgkim.myboard.controller;

import com.cgkim.myboard.exception.AttachNotFoundException;
import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.service.impl.BoardAttachServiceImpl;
import com.cgkim.myboard.vo.attach.AttachVo;
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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/attaches")
public class BoardAttachController {

    private final BoardAttachServiceImpl attachService;

    @Value("${spring.servlet.multipart.location}")
    String basePath;

    /**
     * 파일 다운로드
     */
    //TODO: url 수정
    @GetMapping("/{attachId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long attachId) {
        AttachVo attachVo = attachService.get(attachId);
        Resource resource = new FileSystemResource(getAbsolutePathOf(attachVo));
        if(!resource.exists()) {
            throw new AttachNotFoundException(ErrorCode.ATTACH_NOT_FOUND);
        }
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition
                                .attachment()
                                .filename(attachVo.getFullName(), StandardCharsets.UTF_8)
                                .build()
                                .toString())
                .body(resource);
    }

    /**
     * 이미지 보여주기
     */
    @GetMapping("/{attachId}/display")
    public ResponseEntity<Resource> displayImageAttach(@PathVariable Long attachId) {
        AttachVo attachVo = attachService.get(attachId);
        Resource resource = new FileSystemResource(getThumbnailAbsolutePathOf(attachVo));
        if(!resource.exists()) {
            throw new AttachNotFoundException(ErrorCode.ATTACH_NOT_FOUND);
        }
        return ResponseEntity.ok().body(resource);
    }

    /**
     * 파일 절대경로 리턴
     */
    private String getAbsolutePathOf(AttachVo attachVo) {
        return basePath + File.separator
                + attachVo.getUploadPath() + File.separator
                + attachVo.getUuid() + '.' + attachVo.getExtension();
    }

    /**
     * 첨부파일 절대경로 리턴
     */
    //TODO: 200x200 상수로
    private String getThumbnailAbsolutePathOf(AttachVo attachVo) {
        return basePath + File.separator
                + attachVo.getUploadPath() + File.separator
                + attachVo.getUuid() + "_200x200" + '.' + attachVo.getExtension();
    }
}