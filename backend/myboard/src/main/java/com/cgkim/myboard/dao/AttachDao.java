package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.attach.AttachVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AttachDao {
    int insert(AttachVo attach);
    int selectCountByBoardId(long boardId);

    List<AttachVo> selectList(Long boardId);

    AttachVo selectOne(Long attachId);
    List<AttachVo> select(Long boardId);

    void deleteByBoardId(Long boardId);

    void delete(Long attachId);

}