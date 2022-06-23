package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.board.BoardDetailResponse;
import com.cgkim.myboardadmin.vo.board.BoardListResponse;
import com.cgkim.myboardadmin.vo.board.BoardSearchRequest;
import com.cgkim.myboardadmin.vo.board.BoardVo;
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
    void updateHasAttach(Map<String, Object> updateHasAttachMap);
    void delete(Long boardId);
    int update(Map<String, Object> map);
    void insertBoard(BoardVo boardVo);
    int updateThumbnailUri(Map<String,Object> boardId);
}
