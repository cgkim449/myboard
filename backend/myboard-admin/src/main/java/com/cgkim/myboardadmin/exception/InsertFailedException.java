package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import lombok.Getter;

import java.util.List;

/**
 * 게시물/질문/FAQ/공지 를 DB 에 삽입하던 중 발생한 에러.
 * - 목적: 만일 물리적 파일이 생성되었었다면 그 파일을 삭제해야 하기 때문.
 */
public class InsertFailedException extends BusinessException{

    @Getter
    private final List<AttachVo> attachSaveList;

    /**
     * 물리적 파일이 생성된 첨부파일 목록 및 에러코드 주입
     *
     * @param attachSaveList
     * @param errorCode
     */
    public InsertFailedException(List<AttachVo> attachSaveList, ErrorCode errorCode) {
        super(errorCode);
        this.attachSaveList = attachSaveList;
    }
}
