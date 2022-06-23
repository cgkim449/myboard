package com.cgkim.myboardadmin.exception;


import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import lombok.Getter;

import java.util.List;

public class FAQInsertFailedException extends InsertFailedException{

    public FAQInsertFailedException(List<AttachVo> attachSaveList, ErrorCode errorCode) {
        super(attachSaveList, errorCode);
    }
}
