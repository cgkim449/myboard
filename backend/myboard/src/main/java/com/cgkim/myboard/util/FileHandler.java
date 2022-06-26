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

/**
 * 물리적 파일을 생성 및 삭제하는 역할
 */
@Component
public class FileHandler {

    private final String uploadBasePath; //첨부파일이 저장되는 최상위 경로(C://upload)

    private final String thumbnailFileSuffix;

    /**
     * 첨부파일 경로, 썸네일 파일 접미사 설정 주입
     *
     * @param uploadBasePath
     */
    public FileHandler(@Value("${spring.servlet.multipart.location}") String uploadBasePath,
                       @Value("${thumbnail.file-suffix}") String thumbnailFileSuffix
    ) {

        this.uploadBasePath = uploadBasePath;
        this.thumbnailFileSuffix = thumbnailFileSuffix;
    }


    /**
     * 물리적 파일 생성
     *
     * @param multipartFiles
     * @return List<AttachVo>
     * @throws IOException
     */
    public List<AttachVo> createFiles(MultipartFile[] multipartFiles) throws IOException {

        if(multipartFiles == null) {
            return null;
        }

        String uploadPath = createUploadPath();

        List<AttachVo> attachSaveList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {

            if(!multipartFile.isEmpty()) {

                AttachVo attachVo = buildAttachWith(uploadPath, multipartFile);

                attachSaveList.add(attachVo); // List에 AttachVo 추가

                createFile(uploadPath, multipartFile, attachVo); // 파일 생성
            }
        }

        return attachSaveList;
    }

    private String createUploadPath() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String str = simpleDateFormat.format(new Date());

        String uploadPath = uploadBasePath + File.separator + str.replace("-", File.separator);

        File uploadDir = new File(uploadPath);

        uploadDir.mkdirs();

        return uploadPath;
    }

    private void createFile(String uploadPath, MultipartFile multipartFile, AttachVo attach) throws IOException {

        String saveFileName = attach.getUuid() + "." + attach.getExtension();
        File saveFile = new File(uploadPath, saveFileName);

        multipartFile.transferTo(saveFile); //파일 생성

        if(attach.isImage()) { //이미지 파일이면 썸네일 생성

            saveThumbnail(saveFile);
        }
    }

    private AttachVo buildAttachWith(String uploadPath, MultipartFile multipartFile) {

        uploadPath = uploadPath.replace(uploadBasePath + File.separator, "");

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

        //TODO: 가로 기준으로만 자르기
        Thumbnailator.createThumbnail(originalFileInputStream, thumbnailOutputStream, 200, 200);

        originalFileInputStream.close();
        thumbnailOutputStream.close();
    }

    private String getThumbnailFileName(File originalFile) {

        String saveFileFullName = originalFile.getName();
        String saveFileName = saveFileFullName.substring(0, saveFileFullName.lastIndexOf("."));
        String saveFileExtension = saveFileFullName.substring(saveFileFullName.lastIndexOf('.') + 1);

        return saveFileName + thumbnailFileSuffix + "." + saveFileExtension;
    }

    /**
     * 물리적 파일 삭제
     *
     * @param attachesDeleteRequest
     */
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

        return saveFileAbsolutePath.replace("." + extension, thumbnailFileSuffix + "." + extension);
    }

    private String getSaveFilePath(AttachVo attach) {

        String uploadPath = attach.getUploadPath();
        String uuid = attach.getUuid();
        String attachExtension = attach.getExtension();

        return uploadBasePath +
                File.separator +
                uploadPath +
                File.separator +
                uuid +
                '.' +
                attachExtension;
    }
}
