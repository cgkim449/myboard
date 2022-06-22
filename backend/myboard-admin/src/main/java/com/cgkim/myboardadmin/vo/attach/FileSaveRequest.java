package com.cgkim.myboardadmin.vo.attach;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@ToString
@AllArgsConstructor
@Getter
public class FileSaveRequest {
    private MultipartFile[] multipartFiles;
}
