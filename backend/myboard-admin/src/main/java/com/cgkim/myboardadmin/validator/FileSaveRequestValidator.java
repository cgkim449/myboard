package com.cgkim.myboardadmin.validator;


import com.cgkim.myboardadmin.vo.common.FileSaveRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드시 유효성 검증
 */
@Component
public class FileSaveRequestValidator implements Validator {

    /**
     * 검증 대상 확인
     *
     * @param clazz
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return FileSaveRequest.class.isAssignableFrom(clazz);
    }

    /**
     * 검증
     *
     * @param target
     * @param errors
     */
    @Override
    public void validate(Object target, Errors errors) {

        FileSaveRequest fileSaveRequest = (FileSaveRequest) target;

        MultipartFile[] multipartFiles = fileSaveRequest.getMultipartFiles();

        if (!isValid(multipartFiles)) {
            errors.rejectValue("multipartFiles", "extension");
        }
    }

    private boolean isValid(MultipartFile[] multipartFiles) {

        if (multipartFiles == null) {

            return true;
        }

        for (MultipartFile multipartFile : multipartFiles) {

            if (!multipartFile.isEmpty() && !isValid(multipartFile)) {

                return false;
            }
        }

        return true;
    }

    /**
     * 파일 업로드 확장자 제한 : jsp, jspx, jsw, jsv, jspf, htm, html
     *
     * @param multipartFile
     * @return boolean
     */
    private boolean isValid(MultipartFile multipartFile) {

        String contentType = multipartFile.getContentType();

        if (contentType == null
                || contentType.startsWith("application")
                || contentType.endsWith("html")
                || contentType.endsWith("htm")) {

            return false;

        } else {

            return true;
        }
    }

}
