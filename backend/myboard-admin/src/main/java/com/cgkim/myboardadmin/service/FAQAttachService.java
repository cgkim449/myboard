package com.cgkim.myboardadmin.service;

import com.cgkim.myboardadmin.dao.AnswerAttachDao;
import com.cgkim.myboardadmin.dao.FAQAttachDao;
import com.cgkim.myboardadmin.exception.AttachNotFoundException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * FAQ 첨부파일 Service
 */
@RequiredArgsConstructor
@Service
public class FAQAttachService {

    private final FAQAttachDao attachDao;

    /**
     * 특정 FAQ의 첨부파일 리스트
     *
     * @param faqId
     * @return List<AttachVo>
     */
    public List<AttachVo> getList(Long faqId) {
        return attachDao.selectList(faqId);
    }

    /**
     * 특정 첨부파일
     *
     * @param faqId
     * @return AttachVo
     */
    public AttachVo getAttachBy(Long faqId) {

        AttachVo attachVo = attachDao.selectOne(faqId);

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

        if(attachIdArray == null || attachIdArray.length == 0) {
            return null;
        }

        List<AttachVo> attachVoList = new ArrayList<>();

        for (long attachId : attachIdArray) {
            attachVoList.add(attachDao.selectOne(attachId));
        }

        return attachVoList;
    }
}