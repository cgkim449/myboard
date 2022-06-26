package com.cgkim.myboard.util;

import com.cgkim.myboard.exception.AttachNotFoundException;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 첨부파일 다운로드 요청의 응답메시지를 빌드하는 역할
 */
@RequiredArgsConstructor
@Component
public class AttachDownloadResponseBuilder {

    private final AttachURIProvider attachURIProvider;

    /**
     * 첨부파일 다운로드 요청의 응답메시지를 빌드하는 역할
     *
     * @param attachVo
     * @return ResponseEntity<Resource>
     */
    public ResponseEntity<Resource> buildResponseWith(AttachVo attachVo) {

        Resource resource = findResourceOf(attachVo);

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

    private Resource findResourceOf(AttachVo attachVo) {

        Resource resource = new FileSystemResource(attachURIProvider.getAbsolutePathOf(attachVo));

        if (!resource.exists()) {
            throw new AttachNotFoundException(ErrorCode.ATTACH_NOT_FOUND);
        }

        return resource;
    }


}
