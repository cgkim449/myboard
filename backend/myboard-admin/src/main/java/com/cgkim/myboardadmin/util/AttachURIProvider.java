package com.cgkim.myboardadmin.util;

import com.cgkim.myboardadmin.vo.attach.AttachVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * 첨부파일 관련 URI 들을 제공하는 역할
 */
@Component
public class AttachURIProvider {

    private final String staticResourceURL;

    private final String thumbnailFileSuffix;

    private final String uploadBasePath;

    /**
     * 정적 리소스 url, 첨부파일 업로드 경로, 썸네일 파일명 접미사 설정 주입
     *
     * @param staticResourceURL
     * @param thumbnailFileSuffix
     */
    public AttachURIProvider(@Value("${static-resource.url}") String staticResourceURL,
                             @Value("${thumbnail.file-suffix}") String thumbnailFileSuffix,
                             @Value("${spring.servlet.multipart.location}") String uploadBasePath
    ) {

        this.staticResourceURL = staticResourceURL;
        this.thumbnailFileSuffix = thumbnailFileSuffix;
        this.uploadBasePath = uploadBasePath;
    }

    /**
     * AttachVo 에 원본 이미지 URI 와 썸네일 URI 를 저장
     *
     * @param attachList
     */
    public void setImageURIsOf(List<AttachVo> attachList) {

        for (AttachVo attachVo : attachList) {

            if (attachVo.isImage()) {
                setImageURIOf(attachVo);
            }
        }
    }

    private void setImageURIOf(AttachVo attachVo) {

        attachVo.setThumbnailUri(createThumbnailURIOf(attachVo));
        attachVo.setOriginalImageUri(createOriginalImageURIOf(attachVo));
    }

    private String createOriginalImageURIOf(AttachVo attachVo) {

        return staticResourceURL +
                File.separator +
                attachVo.getUploadPath() +
                File.separator +
                attachVo.getUuid() +
                "." +
                attachVo.getExtension();
    }

    private String createThumbnailURIOf(AttachVo attachVo) {

        return staticResourceURL +
                File.separator +
                attachVo.getUploadPath() +
                File.separator +
                attachVo.getUuid() +
                thumbnailFileSuffix +
                "." +
                attachVo.getExtension();
    }

    /**
     * DB 에 저장할 thumbnail URI 생성
     *
     * @param attachVo
     * @return String
     */
    public String createThumbnailURIForDB(AttachVo attachVo) {

        return attachVo.getUploadPath() + File.separator + attachVo.getUuid() + thumbnailFileSuffix + "." + attachVo.getExtension();
    }

    /**
     * 썸네일 정적 리소스 URI 생성
     *
     * @param thumbnailUri
     * @return String
     */
    public String getFullURIOf(String thumbnailUri) {

        return staticResourceURL + File.separator + thumbnailUri;
    }

    /**
     * 첨부파일의 절대경로 생성
     *
     * @param attachVo
     * @return String
     */
    public String getAbsolutePathOf(AttachVo attachVo) {

        return uploadBasePath +
                File.separator +
                attachVo.getUploadPath() +
                File.separator +
                attachVo.getUuid() +
                '.' +
                attachVo.getExtension();
    }
}
