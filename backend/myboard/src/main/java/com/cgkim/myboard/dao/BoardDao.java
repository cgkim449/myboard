package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.BoardVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BoardDao {
    List<BoardVo> selectList();
}
