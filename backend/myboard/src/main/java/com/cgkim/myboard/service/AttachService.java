package com.cgkim.myboard.service;


import com.cgkim.myboard.vo.attach.AttachVo;

import java.util.List;

public interface AttachService {
    AttachVo get(Long attachNo);
    List<AttachVo> getList(Long boardNo);

    List<AttachVo> getList(Long[] attachNoArray);
}
