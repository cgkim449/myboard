package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardPwCheckRequest;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import com.cgkim.myboard.vo.board.BoardUpdateRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface BoardDao {
    List<BoardListResponse> selectList(BoardSearchRequest boardSearchRequest);
    int selectCount(BoardSearchRequest boardSearchRequest);

    void increaseViewCnt(Long boardId);

    BoardDetailResponse selectOne(Long boardId);

    void insert(BoardSaveRequest boardSaveRequest);

    void updateHasAttach(Map<String, Object> updateHasAttachMap);

    Long selectOneByGuestPassword(BoardPwCheckRequest boardPwCheckRequest);

    void delete(Long boardId);

    int update(BoardUpdateRequest boardUpdateRequest);
}
