package com.cgkim.myboard.exception;

import com.cgkim.myboard.vo.attach.AttachVo;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardInsertFailedException extends BusinessException{

    private List<AttachVo> attachSaveList;

    public BoardInsertFailedException(List<AttachVo> attachSaveList, ErrorCode errorCode) {
        super(errorCode);
        this.attachSaveList = attachSaveList;
    }
}
