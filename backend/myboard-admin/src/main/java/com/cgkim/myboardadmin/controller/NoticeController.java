package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.argumentresolver.LoginUser;
import com.cgkim.myboardadmin.response.SuccessResponse;
import com.cgkim.myboardadmin.service.NoticeAttachService;
import com.cgkim.myboardadmin.service.NoticeService;
import com.cgkim.myboardadmin.util.FileHandler;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.common.FileSaveRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeDetailResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeListResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeSaveRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeSearchRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * 공지사항 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/notices")
public class NoticeController {

    private final NoticeService noticeService;

    private final NoticeAttachService attachService;

    private final FileHandler fileHandler;

    /**
     * 공지 상세 조회
     *
     * @param noticeId
     * @return ResponseEntity<SuccessResponse>
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<SuccessResponse> getDetail(@PathVariable Long noticeId) {

        NoticeDetailResponse noticeDetail = noticeService.viewNoticeDetail(noticeId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("noticeDetail", noticeDetail));
    }

    /**
     * 가장 최근 공지 조회
     *
     * @return ResponseEntity<SuccessResponse>
     */
    @GetMapping("/latest")
    public ResponseEntity<SuccessResponse> getLatestNoticeDetail() {

        NoticeDetailResponse noticeDetail = noticeService.getLatestNoticeDetail();

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("noticeDetail", noticeDetail));
    }

    /**
     * 공지 목록 조회
     *
     * @param noticeSearchRequest
     * @return ResponseEntity<SuccessResponse>
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getList(NoticeSearchRequest noticeSearchRequest) {

        List<NoticeListResponse> noticeList = noticeService.getNoticeList(noticeSearchRequest);
        int noticeTotalCount = noticeService.getTotalCount(noticeSearchRequest);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("noticeList", noticeList)
                        .put("noticeTotalCount", noticeTotalCount));
    }

    /**
     * 공지 작성
     *
     * @param username
     * @param noticeSaveRequest
     * @param fileSaveRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> writeBoard(@LoginUser String username,
                                                      @Valid NoticeSaveRequest noticeSaveRequest,
                                                      @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {

        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());
        Long noticeId = noticeService.write(username, noticeSaveRequest, attachInsertList);

        return ResponseEntity.created(URI.create("/admin/notices/" + noticeId)).body(new SuccessResponse());
    }

    /**
     * 공지 삭제
     *
     * @param noticeId
     * @return ResponseEntity<SuccessResponse>
     */
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<SuccessResponse> delete(@PathVariable Long noticeId) {

        List<AttachVo> attachDeleteList = attachService.getList(noticeId);
        noticeService.delete(noticeId);
        fileHandler.deleteFiles(attachDeleteList);

        return ResponseEntity.noContent().build();
    }

    /**
     * 공지 수정
     *
     * @param noticeId
     * @param noticeUpdateRequest
     * @param fileSaveRequest
     * @param attachDeleteRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException
     */
    @PatchMapping("/{noticeId}")
    public ResponseEntity<SuccessResponse> update(@PathVariable Long noticeId,
                                                  @Valid NoticeUpdateRequest noticeUpdateRequest,
                                                  @Valid FileSaveRequest fileSaveRequest,
                                                  Long[] attachDeleteRequest
    ) throws IOException {

        List<AttachVo> attachDeleteList = attachService.getList(attachDeleteRequest);
        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());

        noticeService.modify(noticeId, noticeUpdateRequest, attachInsertList, attachDeleteList);

        fileHandler.deleteFiles(attachDeleteList);

        return ResponseEntity.ok(new SuccessResponse());
    }
}