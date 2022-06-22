package com.cgkim.myboard.exception;


import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.vo.attach.AttachVo;
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
