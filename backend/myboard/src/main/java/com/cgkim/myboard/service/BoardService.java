package com.cgkim.myboard.service;

import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardPwCheckRequest;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import com.cgkim.myboard.vo.board.BoardUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardService {
    List<BoardListResponse> getBoardList(BoardSearchRequest boardSearchRequest);
    int getTotalCounts(BoardSearchRequest boardSearchRequest);
    BoardDetailResponse viewBoardDetail(Long boardId);

    long write(BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList);
    void pwCheck(Long boardId, String guestPassword);
    void pwCheck(BoardPwCheckRequest boardPwCheckRequest);
    @Transactional(rollbackFor = Exception.class)
    void delete(Long boardNo);

    @Transactional(rollbackFor = Exception.class)
    void modify(BoardUpdateRequest boardUpdateRequest, List<AttachVo> attachSaveList, List<AttachVo> attachDeleteList);
}
