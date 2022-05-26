package com.cgkim.myboard.vo.attach;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttachVo {
    private Long attachId;
    private Long boardId;
    private String attachUploadPath;
    private String attachUuid;
    private String attachName;
    private String attachExtension; // 확장자

    private boolean attachIsImage; // 이미지 여부

    private long attachSize;

    public boolean getAttachIsImage() {
        return attachIsImage;
    }

    public boolean attachIsImage() {
        return attachIsImage;
    }
    @Builder
    public AttachVo(String attachUploadPath, String attachUuid, String attachName, String attachExtension, boolean attachIsImage, long attachSize) {
        this.attachUploadPath = attachUploadPath;
        this.attachUuid = attachUuid;
        this.attachName = attachName;
        this.attachExtension = attachExtension;
        this.attachIsImage = attachIsImage;
        this.attachSize = attachSize;
    }


    public String getFullName() {
        return getAttachName() + '.' + getAttachExtension();
    }

}
