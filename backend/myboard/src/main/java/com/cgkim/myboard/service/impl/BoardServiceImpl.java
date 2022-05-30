package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.AttachDao;
import com.cgkim.myboard.dao.BoardDao;
import com.cgkim.myboard.dao.CommentDao;
import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardPwCheckRequest;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import com.cgkim.myboard.vo.board.BoardUpdateRequest;
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

    @Override
    public Long pwCheck(Long boardId, String guestPassword) {
        return pwCheck(BoardPwCheckRequest.builder().boardId(boardId).guestPassword(guestPassword).build());
    }

    @Override
    public Long pwCheck(BoardPwCheckRequest boardPwCheckRequest) {
        Long result = boardDao.selectOneByGuestPassword(boardPwCheckRequest);
        return result;
        //TODO: 예외처리
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long boardId) {
        commentDao.deleteByBoardId(boardId);
        attachDao.deleteByBoardId(boardId);
        boardDao.delete(boardId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(BoardUpdateRequest boardUpdateRequest, List<AttachVo> attachSaveList, List<AttachVo> attachDeleteList) {

        if(attachDeleteList != null && attachDeleteList.size() > 0) { // 첨부파일 db 삭제
            deleteAttaches(attachDeleteList);
        }

        if(attachSaveList != null && attachSaveList.size() > 0) { // 첨부파일 db 삽입
            insertAttaches(attachSaveList, boardUpdateRequest.getBoardId());
        }

        boardDao.update(boardUpdateRequest);  // 게시물 db 업데이트
        updateHasAttach(boardUpdateRequest.getBoardId());  // 첨부파일 유무 업데이트
    }

    private void deleteAttaches(List<AttachVo> attachDeleteList) {
        for (AttachVo attachVo : attachDeleteList) {
            attachDao.delete(attachVo.getAttachId());
        }
    }
}
