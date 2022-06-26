package com.cgkim.myboard.service;

import com.cgkim.myboard.dao.NoticeAttachDao;
import com.cgkim.myboard.exception.AttachNotFoundException;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 공지사항 첨부파일 Service
 */
@RequiredArgsConstructor
@Service
public class NoticeAttachService {

    private final NoticeAttachDao attachDao;

    /**
     * 특정 첨부파일 조회
     *
     * @param attachId
     * @return AttachVo
     */
    public AttachVo getAttachBy(Long attachId) {

        AttachVo attachVo = attachDao.selectOne(attachId);

        if (attachVo == null) {
            throw new AttachNotFoundException(ErrorCode.ATTACH_NOT_FOUND);
        }

        return attachVo;
    }
}