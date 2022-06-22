package com.cgkim.myboard.util;

import com.cgkim.myboard.vo.attach.AttachVo;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//TODO: 리팩토링

@Component
public class FileHandler {

    private static String basePath; // 첨부파일 저장할 최상위 폴더(C://upload)

    @Value("${spring.servlet.multipart.location}")
    public void setBasePath(String basePath) {
        FileHandler.basePath = basePath;
    }

    public List<AttachVo> createFiles(MultipartFile[] multipartFiles) throws IOException {
        if(multipartFiles == null) {
            return null;
        }

        String uploadPath = createUploadPath();

        List<AttachVo> attachesSaveRequest = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()) {

                AttachVo attachVo = buildAttach(uploadPath, multipartFile);
                attachesSaveRequest.add(attachVo); // List에 AttachVo 추가

                saveFile(uploadPath, multipartFile, attachVo); // 파일 생성
            }
        }
        return attachesSaveRequest;
    }

    private static String createUploadPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String str = sdf.format(new Date());
        String uploadPath = basePath + File.separator + str.replace("-", File.separator);

        File uploadDir = new File(uploadPath);
        uploadDir.mkdirs();

        return uploadPath;
    }

    private void saveFile(String uploadPath, MultipartFile multipartFile, AttachVo attach) throws IOException {
        String saveFileName = attach.getUuid() + "." + attach.getExtension();
        File saveFile = new File(uploadPath, saveFileName);

        multipartFile.transferTo(saveFile); //파일 생성
        if(attach.isImage()) { //이미지 파일이면 썸네일 생성
            saveThumbnail(saveFile);
        }
    }

    private AttachVo buildAttach(String uploadPath, MultipartFile multipartFile) {
        uploadPath = uploadPath.replace(basePath + File.separator, "");

        UUID uuid = UUID.randomUUID();

        String fileFullName = multipartFile.getOriginalFilename();
        String fileName = fileFullName.substring(0, fileFullName.lastIndexOf('.'));
        String fileExtension = fileFullName.substring(fileFullName.lastIndexOf('.') + 1);

        long fileSize = multipartFile.getSize();
        boolean isImage = multipartFile.getContentType().startsWith("image");

        return AttachVo.builder()
                .uploadPath(uploadPath)
                .uuid(uuid.toString())
                .name(fileName)
                .extension(fileExtension)
                .size(fileSize)
                .isImage(isImage)
                .build();
    }

    private void saveThumbnail(File originalFile) throws IOException {
        File thumbnailFile = new File(originalFile.getParent(), getThumbnailFileName(originalFile));

        OutputStream thumbnailOutputStream = new FileOutputStream(thumbnailFile);
        InputStream originalFileInputStream = new FileInputStream(originalFile);

        Thumbnailator.createThumbnail(originalFileInputStream, thumbnailOutputStream, 200, 200);
//TODO: 가로 기준으로만 자르기
        originalFileInputStream.close();
        thumbnailOutputStream.close();
    }

    private String getThumbnailFileName(File originalFile) {
        String saveFileFullName = originalFile.getName();
        String saveFileName = saveFileFullName.substring(0, saveFileFullName.lastIndexOf("."));
        String saveFileExtension = saveFileFullName.substring(saveFileFullName.lastIndexOf('.') + 1);

        return saveFileName + "_thumbnail" + "." + saveFileExtension;
    }

    public void deleteFiles(List<AttachVo> attachesDeleteRequest){
        if(attachesDeleteRequest == null || attachesDeleteRequest.isEmpty()) {
            return;
        }

        for (AttachVo attach : attachesDeleteRequest) {

            String saveFilePath = getSaveFilePath(attach);
            new File(saveFilePath).delete();

            if(attach.isImage()) { // 이미지인 경우 썸네일 삭제

                String thumbnailFilePath = getThumbnailFilePath(saveFilePath);
                new File(thumbnailFilePath).delete();
            }
        }
    }

    private String getThumbnailFilePath(String saveFileAbsolutePath) {
        String extension = saveFileAbsolutePath.substring(saveFileAbsolutePath.lastIndexOf('.') + 1);

        return saveFileAbsolutePath.replace("." + extension, "_200x200" + "." + extension);
    }

    private String getSaveFilePath(AttachVo attach) {
        String uploadPath = attach.getUploadPath();
        String uuid = attach.getUuid();
        String attachName = attach.getName();
        String attachExtension = attach.getExtension();

        return basePath + File.separator
                + uploadPath + File.separator
                + uuid + '_'
                + attachName + '.'
                + attachExtension;
    }
}
