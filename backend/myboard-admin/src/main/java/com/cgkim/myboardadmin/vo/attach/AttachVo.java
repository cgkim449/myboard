package com.cgkim.myboardadmin.vo.attach;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 첨부파일 VO
 */
@Data
@NoArgsConstructor
public class AttachVo {

    private Long attachId;

    private Long boardId;

    private Long questionId;

    private Long answerId;

    private Long faqId;

    private Long noticeId;
    private String uploadPath;
    private String uuid;
    private String name;
    private String extension; // 확장자

    private boolean isImage; // 이미지 여부

    private long size;

    /**
     * 이미지 여부
     *
     * @return boolean
     */
    public boolean getIsImage() {
        return isImage;
    }

    /**
     * 이미지 여부
     *
     * @return boolean
     */
    public boolean isImage() {
        return isImage;
    }

    private String thumbnailUri;
    private String originalImageUri;

    /**
     * 주입
     *
     * @param uploadPath
     * @param uuid
     * @param name
     * @param extension
     * @param isImage
     * @param size
     */
    @Builder
    public AttachVo(String uploadPath, String uuid, String name, String extension, boolean isImage, long size) {
        this.uploadPath = uploadPath;
        this.uuid = uuid;
        this.name = name;
        this.extension = extension;
        this.isImage = isImage;
        this.size = size;
    }

    /**
     * 첨부파일 이름 + 확장자 리턴
     *
     * @return String
     */
    public String getFullName() {
        return getName() + '.' + getExtension();
    }
}
