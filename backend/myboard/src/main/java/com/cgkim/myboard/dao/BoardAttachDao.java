package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.attach.AttachVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BoardAttachDao {
    List<AttachVo> selectList(Long boardId);
    AttachVo selectOne(Long attachId);
    int selectCountByBoardId(long boardId);
    int insert(AttachVo attach);
    void deleteByBoardId(Long boardId);
    void delete(Long attachId);

}