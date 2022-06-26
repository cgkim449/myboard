package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import lombok.Getter;

import java.util.List;

/**
 * 게시물 등록 실패
 */
public class BoardInsertFailedException extends InsertFailedException{

    /**
     * 물리적 파일 생성했던 첨부파일 목록과 에러코드 주입
     *
     * @param attachSaveList
     * @param errorCode
     */
    public BoardInsertFailedException(List<AttachVo> attachSaveList, ErrorCode errorCode) {
        super(attachSaveList, errorCode);
    }
}
