package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.AttachDao;
import com.cgkim.myboard.service.AttachService;
import com.cgkim.myboard.vo.attach.AttachVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AttachServiceImpl implements AttachService {
    private final AttachDao attachDao;

    /**
     * 첨부파일 하나 select
     *
     * @param attachNo
     * @return
     */
    @Override
    public AttachVo get(Long attachId) {
        return attachDao.selectOne(attachId);
    }

}