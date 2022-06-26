package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.board.BoardDetailResponse;
import com.cgkim.myboardadmin.vo.board.BoardListResponse;
import com.cgkim.myboardadmin.vo.board.BoardSearchRequest;
import com.cgkim.myboardadmin.vo.board.BoardUpdateRequest;
import com.cgkim.myboardadmin.vo.board.BoardVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 자유게시판 DAO
 */
@Mapper
@Repository
public interface BoardDao {

    /**
     * 검색조건에 해당하는 게시물 목록 select
     *
     * @param boardSearchRequest
     * @return List<BoardListResponse>
     */
    List<BoardListResponse> selectList(BoardSearchRequest boardSearchRequest);

    /**
     * 검색조건에 해당하는 게시물 갯수 select
     *
     * @param boardSearchRequest
     * @return int
     */
    int selectCount(BoardSearchRequest boardSearchRequest);

    /**
     * 조회수 증가
     *
     * @param boardId
     */
    void increaseViewCnt(Long boardId);

    /**
     * 게시글 상세 조회
     *
     * @param boardId
     * @return BoardDetailResponse
     */
    BoardDetailResponse selectOne(Long boardId);

    /**
     * 첨부파일 유무 업데이트
     *
     * @param updateHasAttachMap
     */
    void updateHasAttach(Map<String, Object> updateHasAttachMap);

    /**
     * 게시글 삭제
     *
     * @param boardId
     */
    void delete(Long boardId);

    /**
     * 게시글 수정
     *
     * @param boardUpdateRequest
     * @return int
     */
    int update(BoardUpdateRequest boardUpdateRequest);

    /**
     * 관리자 게시글 삽입
     *
     * @param boardVo
     */
    void insertBoard(BoardVo boardVo);

    /**
     * 게시글 썸네일 uri 업데이트
     *
     * @param updateThumbnailUriMap
     * @return int
     */
    int updateThumbnailUri(Map<String,Object> updateThumbnailUriMap);
}
