package com.cgkim.myboard.vo.attach;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttachVo {
    private Long attachId;
    private Long boardId;
    private String uploadPath;
    private String uuid;
    private String name;
    private String extension; // 확장자

    private boolean isImage; // 이미지 여부

    private long size;

    public boolean getIsImage() {
        return isImage;
    }

    public boolean isImage() {
        return isImage;
    }
    @Builder
    public AttachVo(String uploadPath, String uuid, String name, String extension, boolean isImage, long size) {
        this.uploadPath = uploadPath;
        this.uuid = uuid;
        this.name = name;
        this.extension = extension;
        this.isImage = isImage;
        this.size = size;
    }


    public String getFullName() {
        return getName() + '.' + getExtension();
    }

}
