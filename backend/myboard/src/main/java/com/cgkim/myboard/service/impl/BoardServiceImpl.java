package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.AttachDao;
import com.cgkim.myboard.dao.BoardDao;
import com.cgkim.myboard.dao.CommentDao;
import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardDao boardDao;
    private final CommentDao commentDao;
    private final AttachDao attachDao;

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
        boardDetailResponse.setAttachList(attachDao.selectList(boardId)); // 첨부파일 리스트
        boardDetailResponse.setCommentList(commentDao.selectList(boardId)); // 댓글 리스트
        return boardDetailResponse;
    }

    //TODO: 예외처리
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long write(BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList) {
        boardDao.insert(boardSaveRequest); // 게시글 insert
        long boardId = boardSaveRequest.getBoardId();

        if (attachInsertList != null && !attachInsertList.isEmpty()) {
            insertAttaches(attachInsertList, boardSaveRequest.getBoardId());  // 첨부파일 insert
            updateHasAttach(boardId);
        }

        return boardId; // 등록한 게시글 번호 리턴
    }

    private void insertAttaches(List<AttachVo> attachInsertList, Long boardId) {
        for (AttachVo attach : attachInsertList) {
            attach.setBoardId(boardId);
            attachDao.insert(attach);
        }
    }

    private void updateHasAttach(long boardId) {
        int attachCount = attachDao.selectCountByBoardId(boardId);

        boardDao.updateHasAttach(
                Map.of("boardId", boardId,
                        "boardHasAttach", attachCount > 0));
    }
}
