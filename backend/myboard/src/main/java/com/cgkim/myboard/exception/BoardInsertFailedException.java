package com.cgkim.myboard.exception;

import com.cgkim.myboard.vo.attach.AttachVo;
import lombok.Getter;

import java.util.List;

public class BoardInsertFailedException extends BusinessException{

    @Getter
    private final List<AttachVo> attachSaveList;

    public BoardInsertFailedException(List<AttachVo> attachSaveList, ErrorCode errorCode) {
        super(errorCode);
        this.attachSaveList = attachSaveList;
    }
}
