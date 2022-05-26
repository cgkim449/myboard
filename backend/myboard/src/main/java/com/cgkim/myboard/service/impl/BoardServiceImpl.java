package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.BoardDao;
import com.cgkim.myboard.dao.CommentDao;
import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardDao boardDao;
    private final CommentDao commentDao;

    @Override
    public List<BoardListResponse> getBoardList(BoardSearchRequest boardSearchRequest) {
        return boardDao.selectList(boardSearchRequest);
    }

    @Override
    public int getTotalCounts(BoardSearchRequest boardSearchRequest) {
        return boardDao.selectCount(boardSearchRequest);
    }

    @Override
    public BoardDetailResponse viewBoardDetail(Long boardId) {
        boardDao.increaseViewCnt(boardId); // 조회수 1 증가

        BoardDetailResponse boardDetailResponse = boardDao.selectOne(boardId); // 게시글
//        boardDetailResponse.setAttachList(attachDao.selectList(boardId)); // 첨부파일 리스트
        boardDetailResponse.setCommentList(commentDao.selectList(boardId)); // 댓글 리스트
        return boardDetailResponse;
    }
}
