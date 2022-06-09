package com.cgkim.myboard.service;

import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import com.cgkim.myboard.vo.member.GuestSaveRequest;
import com.cgkim.myboard.vo.question.QuestionDetailResponse;
import com.cgkim.myboard.vo.question.QuestionListResponse;
import com.cgkim.myboard.vo.question.QuestionSaveRequest;
import com.cgkim.myboard.vo.question.QuestionSearchRequest;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface QuestionService {
    List<QuestionListResponse> getQuestionList(QuestionSearchRequest questionSearchRequest);

    int getTotalCounts(QuestionSearchRequest questionSearchRequest);

    @Transactional(rollbackFor = Exception.class)
    long write(Long memberId, QuestionSaveRequest questionSaveRequest, List<AttachVo> attachInsertList);

    QuestionDetailResponse viewDetail(Long id);
//
//    @Transactional(rollbackFor = Exception.class)
//    long write(GuestSaveRequest guestSaveRequest, BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList);
//
//
//    void checkGuestPassword(Long boardId, String guestPassword) throws NoSuchAlgorithmException;
//
//    @Transactional(rollbackFor = Exception.class)
//    void delete(Long boardNo);
//
//    @Transactional(rollbackFor = Exception.class)
//    void modify(Long boardId,
//                String boardContent,
//                String boardTitle,
//                List<AttachVo> attachInsertList,
//                List<AttachVo> attachDeleteList);
//
//    boolean isAnonymous(Long boardId);
}
