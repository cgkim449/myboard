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

    @Override
    public AttachVo get(Long attachId) {
        return attachDao.selectOne(attachId);
    }

    @Override
    public List<AttachVo> getList(Long boardId) {
        return attachDao.select(boardId);
    }

    @Override
    public List<AttachVo> getList(Long[] attachIdArray) {
        if(attachIdArray == null || attachIdArray.length == 0) {
            return null;
        }

        List<AttachVo> attachVoList = new ArrayList<>();

        for (long attachId : attachIdArray) {
            attachVoList.add(attachDao.selectOne(attachId));
        }

        return attachVoList;
    }
}