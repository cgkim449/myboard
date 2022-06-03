package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.AttachDao;
import com.cgkim.myboard.dao.BoardDao;
import com.cgkim.myboard.dao.CommentDao;
import com.cgkim.myboard.exception.BoardInsertFailedException;
import com.cgkim.myboard.exception.GuestPasswordMismatchException;
import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.util.SHA256PasswordEncoder;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import com.cgkim.myboard.vo.board.BoardVo;
import com.cgkim.myboard.vo.user.GuestSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardDao boardDao;
    private final CommentDao commentDao;
    private final AttachDao attachDao;
    private final SHA256PasswordEncoder sha256PasswordEncoder;

    /**
     * 검색조건에 해당하는 게시물 리스트
     */
    @Override
    public List<BoardListResponse> getBoardList(BoardSearchRequest boardSearchRequest) {
        return boardDao.selectList(boardSearchRequest);
    }

    /**
     * 검색조건에 해당하는 게시물 총 갯수
     */
    @Override
    public int getTotalCounts(BoardSearchRequest boardSearchRequest) {
        return boardDao.selectCount(boardSearchRequest);
    }

    /**
     * 게시물 상세 보기
     */
    @Override
    public BoardDetailResponse viewBoardDetail(Long boardId) {
        boardDao.increaseViewCnt(boardId); //조회수 1 증가

        BoardDetailResponse boardDetailResponse = boardDao.selectOne(boardId); //게시글
        boardDetailResponse.setAttachList(attachDao.selectList(boardId)); //첨부파일 리스트
        boardDetailResponse.setCommentList(commentDao.selectList(boardId)); //댓글 리스트
        return boardDetailResponse;
    }

    /**
     * 익명 게시물 등록
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public long write(GuestSaveRequest guestSaveRequest, BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList) {
        try {
            BoardVo boardVo = BoardVo.builder()
                    .categoryId(boardSaveRequest.getCategoryId())
                    .boardTitle(boardSaveRequest.getBoardTitle())
                    .boardContent(boardSaveRequest.getBoardContent())
                    .guestNickname(guestSaveRequest.getGuestNickname())
                    .guestPassword(sha256PasswordEncoder.getHash(guestSaveRequest.getGuestPassword()))
                    .build();

            boardDao.insertGuestBoard(boardVo); //게시물 insert
            long boardId = boardVo.getBoardId();

            if (attachInsertList != null && !attachInsertList.isEmpty()) {
                insertAttaches(attachInsertList, boardId);  //첨부파일 insert
                updateHasAttach(boardId);
            }

            return boardId; //등록한 게시물 번호 리턴
        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제하기 위해
            throw new BoardInsertFailedException(attachInsertList, ErrorCode.BOARD_INSERT_FAILED);
        }
    }

    /**
     * 로그인 사용자 게시물 등록
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public long write(Long userId, BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList) {
        try {
            BoardVo boardVo = BoardVo.builder()
                    .categoryId(boardSaveRequest.getCategoryId())
                    .boardTitle(boardSaveRequest.getBoardTitle())
                    .boardContent(boardSaveRequest.getBoardContent())
                    .userId(userId)
                    .build();

            boardDao.insertLoginUserBoard(boardVo); //게시물 insert
            long boardId = boardVo.getBoardId();

            if (attachInsertList != null && !attachInsertList.isEmpty()) {
                insertAttaches(attachInsertList, boardId);  //첨부파일 insert
                updateHasAttach(boardId);
            }

            return boardId; //등록한 게시물 번호 리턴
        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제하기 위해
            throw new BoardInsertFailedException(attachInsertList, ErrorCode.BOARD_INSERT_FAILED);
        }
    }

    /**
     * 게시물 수정
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(
            Long boardId,
            String boardContent,
            String boardTitle,
            List<AttachVo> attachInsertList,
            List<AttachVo> attachDeleteList
    ) {
        if(attachDeleteList != null && attachDeleteList.size() > 0) { //첨부파일 delete
            deleteAttaches(attachDeleteList);
        }

        if(attachInsertList != null && attachInsertList.size() > 0) { //첨부파일 insert
            insertAttaches(attachInsertList, boardId);
        }

        boardDao.update(
                Map.of(
                        "boardId", boardId,
                        "boardTitle", boardTitle,
                        "boardContent", boardContent
                )
        );  //게시물 update
        updateHasAttach(boardId);  //첨부파일 유무 update
    }

    @Override
    public boolean isAnonymous(Long boardId) {
        return boardDao.selectUserId(boardId) == null;
    }

    /**
     * 게시물 삭제
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long boardId) {
        commentDao.deleteByBoardId(boardId);
        attachDao.deleteByBoardId(boardId);
        boardDao.delete(boardId);
    }

    /**
     * 익명 게시물 비밀번호 확인
     */
    @Override
    public void checkGuestPassword(Long boardId, String guestPassword) throws NoSuchAlgorithmException {
        Long result = boardDao.selectOneByGuestPassword(
                Map.of(
                        "boardId", boardId,
                        "guestPassword", sha256PasswordEncoder.getHash(guestPassword)
                )
        );

        if(result == null) {
            throw new GuestPasswordMismatchException(ErrorCode.GUEST_PASSWORD_MISMATCH);
        }
    }

    /**
     * 첨부파일 insert
     */
    private void insertAttaches(List<AttachVo> attachInsertList, Long boardId) {
        for (AttachVo attach : attachInsertList) {
            attach.setBoardId(boardId);
            attachDao.insert(attach);
        }
    }

    /**
     * 첨부파일 delete
     */
    private void deleteAttaches(List<AttachVo> attachDeleteList) {
        for (AttachVo attachVo : attachDeleteList) {
            attachDao.delete(attachVo.getAttachId());
        }
    }

    /**
     * 첨부파일 유무 여부 update
     */
    private void updateHasAttach(long boardId) {
        int attachCount = attachDao.selectCountByBoardId(boardId);

        boardDao.updateHasAttach(
                Map.of(
                        "boardId", boardId,
                        "boardHasAttach", attachCount > 0
                )
        );
    }

}
