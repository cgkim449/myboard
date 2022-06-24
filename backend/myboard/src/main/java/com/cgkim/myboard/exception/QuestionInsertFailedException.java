package com.cgkim.myboard.exception;



import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.vo.attach.AttachVo;

import java.util.List;

public class QuestionInsertFailedException extends InsertFailedException{

    public QuestionInsertFailedException(List<AttachVo> attachSaveList, ErrorCode errorCode) {
        super(attachSaveList, errorCode);
    }
}