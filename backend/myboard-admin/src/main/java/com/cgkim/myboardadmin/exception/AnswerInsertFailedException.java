package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import lombok.Getter;

import java.util.List;

public class AnswerInsertFailedException extends BusinessException{

    @Getter
    private final List<AttachVo> attachSaveList;

    public AnswerInsertFailedException(List<AttachVo> attachSaveList, ErrorCode errorCode) {
        super(errorCode);
        this.attachSaveList = attachSaveList;
    }
}
