package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.attach.AttachVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface QuestionAttachDao {
    List<AttachVo> selectList(Long id);
    AttachVo selectOne(Long attachId);
    int selectCountById(long id);
    int insert(AttachVo attach);
    void deleteById(Long id);
    void delete(Long attachId);

}