package com.cgkim.myboard.service;

import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSearchRequest;

import java.util.List;

public interface BoardService {
    List<BoardListResponse> getBoardList(BoardSearchRequest boardSearchRequest);
    int getTotalCounts(BoardSearchRequest boardSearchRequest);
}
