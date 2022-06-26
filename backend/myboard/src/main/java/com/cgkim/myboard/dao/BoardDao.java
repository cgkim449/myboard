package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import com.cgkim.myboard.vo.board.BoardUpdateRequest;
import com.cgkim.myboard.vo.board.BoardVo;
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
     * 익명 게시글 비밀번호 체크
     *
     * @param map
     * @return Long
     */
    Long selectBoardIdByGuestPassword(Map<String, Object> map);

    /**
     * 게시글 삭제
     *
     * @param boardId
     */
    void deleteByBoardId(Long boardId);

    /**
     * 게시글 수정
     *
     * @param boardUpdateRequest
     * @return int
     */
    int update(BoardUpdateRequest boardUpdateRequest);

    /**
     * 익명 게시글 삽입
     *
     * @param boardVo
     */
    void insertGuestBoard(BoardVo boardVo);

    /**
     * 관리자 게시글 삽입
     *
     * @param boardVo
     */
    void insertAdminBoard(BoardVo boardVo);

    /**
     * 회원 게시글 삽입
     *
     * @param boardVo
     */
    void insertMemberBoard(BoardVo boardVo);

    /**
     * 게시글 썸네일 uri 업데이트
     *
     * @param updateThumbnailUriMap
     * @return int
     */
    int updateThumbnailUri(Map<String, Object> updateThumbnailUriMap);

    /**
     * 게시글 작성자(회원) ID 조회
     *
     * @param boardId
     * @return Long
     */
    Long selectMemberId(Long boardId);

    /**
     * 익명 게시글 별명 조회
     *
     * @param boardId
     * @return String
     */
    String selectGuestNickname(Long boardId);

    /**
     * 게시글 작성자(관리자) ID 조회
     *
     * @param boardId
     * @return Long
     */
    Long selectAdminId(Long boardId);
}
