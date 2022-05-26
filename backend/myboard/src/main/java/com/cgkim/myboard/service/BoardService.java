package com.cgkim.myboard.service;

import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.board.BoardSearchRequest;

import java.util.List;

public interface BoardService {
    List<BoardListResponse> getBoardList(BoardSearchRequest boardSearchRequest);
    int getTotalCounts(BoardSearchRequest boardSearchRequest);
    BoardDetailResponse viewBoardDetail(Long boardId);

    long write(BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList);
}
