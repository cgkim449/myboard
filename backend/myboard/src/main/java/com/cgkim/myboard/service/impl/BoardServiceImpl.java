package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.AttachDao;
import com.cgkim.myboard.dao.BoardDao;
import com.cgkim.myboard.dao.CommentDao;
import com.cgkim.myboard.exception.BoardInsertFailedException;
import com.cgkim.myboard.exception.BoardPasswordIncorrectException;
import com.cgkim.myboard.exception.ErrorCode;
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

    /**
     * 검색조건에 해당하는 게시물 리스트
     *
     * @param boardSearchRequest
     * @return
     */
    @Override
    public List<BoardListResponse> getBoardList(BoardSearchRequest boardSearchRequest) {
        return boardDao.selectList(boardSearchRequest);
    }

    /**
     * 검색조건에 해당하는 게시물 총 갯수
     * @param boardSearchRequest
     * @return
     */
    @Override
    public int getTotalCounts(BoardSearchRequest boardSearchRequest) {
        return boardDao.selectCount(boardSearchRequest);
    }

    /**
     * 게시물 상세 보기
     *
     * @param boardId
     * @return
     */
    @Override
    public BoardDetailResponse viewBoardDetail(Long boardId) {
        boardDao.increaseViewCnt(boardId); // 조회수 1 증가

        BoardDetailResponse boardDetailResponse = boardDao.selectOne(boardId); // 게시글
        boardDetailResponse.setAttachList(attachDao.selectList(boardId)); // 첨부파일 리스트
        boardDetailResponse.setCommentList(commentDao.selectList(boardId)); // 댓글 리스트
        return boardDetailResponse;
    }

    /**
     * 게시물 등록
     *
     * @param boardSaveRequest
     * @param attachInsertList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long write(BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList) {
        try {
            boardDao.insert(boardSaveRequest); // 게시물 insert
            long boardId = boardSaveRequest.getBoardId();

            if (attachInsertList != null && !attachInsertList.isEmpty()) {
                insertAttaches(attachInsertList, boardSaveRequest.getBoardId());  // 첨부파일 insert
                updateHasAttach(boardId);
            }

            return boardId; // 등록한 게시물 번호 리턴
        } catch (Exception e) { // 게시물 등록 실패시 생성했던 파일 삭제하기 위해
            throw new BoardInsertFailedException(attachInsertList, ErrorCode.BOARD_INSERT_FAILED);
        }
    }

    /**
     * 게시물 수정
     *
     * @param boardUpdateRequest
     * @param attachSaveList
     * @param attachDeleteList
     */
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
        updateHasAttach(boardUpdateRequest.getBoardId());  // 첨부파일 유무 여부 업데이트
    }

    /**
     * 게시물 삭제
     *
     * @param boardId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long boardId) {
        commentDao.deleteByBoardId(boardId);
        attachDao.deleteByBoardId(boardId);
        boardDao.delete(boardId);
    }

    /**
     * 게시물 비밀번호 확인
     *
     * @param boardId
     * @param guestPassword
     */
    @Override
    public void pwCheck(Long boardId, String guestPassword) {
        pwCheck(BoardPwCheckRequest.builder().boardId(boardId).guestPassword(guestPassword).build());
    }

    /**
     * 게시물 비밀번호 확인
     *
     * @param boardPwCheckRequest
     */
    @Override
    public void pwCheck(BoardPwCheckRequest boardPwCheckRequest) {
        Long result = boardDao.selectOneByGuestPassword(boardPwCheckRequest);
        if(result == null) {
            throw new BoardPasswordIncorrectException(ErrorCode.BOARD_PASSWORD_INCORRECT);
        }
    }


    /**
     * 첨부파일 insert
     *
     * @param attachInsertList
     * @param boardId
     */
    private void insertAttaches(List<AttachVo> attachInsertList, Long boardId) {
        for (AttachVo attach : attachInsertList) {
            attach.setBoardId(boardId);
            attachDao.insert(attach);
        }
    }

    /**
     * 첨부파일 delete
     *
     * @param attachDeleteList
     */
    private void deleteAttaches(List<AttachVo> attachDeleteList) {
        for (AttachVo attachVo : attachDeleteList) {
            attachDao.delete(attachVo.getAttachId());
        }
    }

    /**
     * 첨부파일 유무 여부 update
     *
     * @param boardId
     */
    private void updateHasAttach(long boardId) {
        int attachCount = attachDao.selectCountByBoardId(boardId);

        boardDao.updateHasAttach(
                Map.of("boardId", boardId,
                        "boardHasAttach", attachCount > 0));
    }

}
