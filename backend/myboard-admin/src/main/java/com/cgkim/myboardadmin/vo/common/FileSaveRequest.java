package com.cgkim.myboardadmin.vo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * 첨부파일 업로드 요청
 */
@ToString
@AllArgsConstructor
@Getter
public class FileSaveRequest {

    private MultipartFile[] multipartFiles;
}
