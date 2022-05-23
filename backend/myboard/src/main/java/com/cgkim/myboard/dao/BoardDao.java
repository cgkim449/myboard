package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BoardDao {
    List<BoardListResponse> selectList(BoardSearchRequest boardSearchRequest);
    int selectCount(BoardSearchRequest boardSearchRequest);
}
