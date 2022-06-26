package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import lombok.Getter;

import java.util.List;

/**
 * FAQ 등록 실패
 */
public class FAQInsertFailedException extends InsertFailedException{

    /**
     * 물리적 파일이 생성된 첨부파일 목록 및 에러코드 주입
     *
     * @param attachSaveList
     * @param errorCode
     */
    public FAQInsertFailedException(List<AttachVo> attachSaveList, ErrorCode errorCode) {
        super(attachSaveList, errorCode);
    }
}