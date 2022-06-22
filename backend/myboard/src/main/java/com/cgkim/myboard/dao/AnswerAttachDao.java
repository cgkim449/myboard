package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.attach.AttachVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface AnswerAttachDao {

    List<AttachVo> selectList(Long id);
    AttachVo selectOne(Long attachId);
    int selectCountByAnswerId(long id);
    int insert(AttachVo attach);
    void deleteByAnswerId(Long boardId);
    void delete(Long attachId);
}