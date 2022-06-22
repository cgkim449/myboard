package com.cgkim.myboardadmin.service;

import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.board.BoardDetailResponse;
import com.cgkim.myboardadmin.vo.board.BoardListResponse;
import com.cgkim.myboardadmin.vo.board.BoardSaveRequest;
import com.cgkim.myboardadmin.vo.board.BoardSearchRequest;
import com.cgkim.myboardadmin.vo.member.GuestSaveRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface BoardService {
    List<BoardListResponse> getBoardList(BoardSearchRequest boardSearchRequest);

    int getTotalCount(BoardSearchRequest boardSearchRequest);

    BoardDetailResponse viewBoardDetail(Long boardId);

    @Transactional(rollbackFor = Exception.class)
    long write(String username, BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList);


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
