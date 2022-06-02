package com.cgkim.myboard.service;


import com.cgkim.myboard.vo.attach.AttachVo;

import java.util.List;

public interface AttachService {
    AttachVo get(Long attachId);
    List<AttachVo> getList(Long boardId);

    List<AttachVo> getList(Long[] attachIdArray);
}
