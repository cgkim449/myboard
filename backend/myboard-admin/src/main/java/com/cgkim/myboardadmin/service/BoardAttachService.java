package com.cgkim.myboardadmin.service;

import com.cgkim.myboardadmin.dao.BoardAttachDao;
import com.cgkim.myboardadmin.exception.AttachNotFoundException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 자유게시판 첨부파일 Service
 */
@RequiredArgsConstructor
@Service
public class BoardAttachService {

    private final BoardAttachDao attachDao;

    /**
     * 특정 게시물의 첨부파일 리스트 조회
     *
     * @param boardId
     * @return List<AttachVo>
     */
    public List<AttachVo> getList(Long boardId) {
        return attachDao.selectList(boardId);
    }

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

    /**
     * 첨부파일 id 배열로 첨부파일 리스트 조회
     *
     * @param attachIdArray
     * @return List<AttachVo>
     */
    public List<AttachVo> getList(Long[] attachIdArray) {

        if (attachIdArray == null || attachIdArray.length == 0) {
            return null;
        }

        List<AttachVo> attachVoList = new ArrayList<>();

        for (Long attachId : attachIdArray) {
            attachVoList.add(attachDao.selectOne(attachId));
        }

        return attachVoList;
    }
}