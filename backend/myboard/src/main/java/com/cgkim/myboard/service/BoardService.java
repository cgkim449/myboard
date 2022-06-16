package com.cgkim.myboard.service;

import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import com.cgkim.myboard.vo.member.GuestSaveRequest;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface BoardService {
    List<BoardListResponse> getBoardList(BoardSearchRequest boardSearchRequest);

    int getTotalCounts(BoardSearchRequest boardSearchRequest);

    BoardDetailResponse viewBoardDetail(Long boardId);

    @Transactional(rollbackFor = Exception.class)
    long write(GuestSaveRequest guestSaveRequest, BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList);
    @Transactional(rollbackFor = Exception.class)
    long write(String username, boolean isAdmin, BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList);

    @Transactional(rollbackFor = Exception.class)
    long write(Long memberId, BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList);


    void checkGuestPassword(Long boardId, String guestPassword) throws NoSuchAlgorithmException;

    @Transactional(rollbackFor = Exception.class)
    void delete(Long boardNo);

    @Transactional(rollbackFor = Exception.class)
    void modify(Long boardId,
                String boardContent,
                String boardTitle,
                List<AttachVo> attachInsertList,
                List<AttachVo> attachDeleteList);

    boolean isAnonymous(Long boardId);

    void checkOwner(Long boardId, String username, String guestPassword) throws NoSuchAlgorithmException;



}
