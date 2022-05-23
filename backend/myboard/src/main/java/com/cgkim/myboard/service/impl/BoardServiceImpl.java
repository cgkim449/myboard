package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.BoardDao;
import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    final BoardDao boardDao;

    @Override
    public List<BoardListResponse> getBoardList(BoardSearchRequest boardSearchRequest) {
        return boardDao.selectList(boardSearchRequest);
    }

    @Override
    public int getTotalCounts(BoardSearchRequest boardSearchRequest) {
        return boardDao.selectCount(boardSearchRequest);
    }
}
