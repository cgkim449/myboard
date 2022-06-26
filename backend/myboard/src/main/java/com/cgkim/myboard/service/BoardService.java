package com.cgkim.myboard.service;

import com.cgkim.myboard.dao.BoardAttachDao;
import com.cgkim.myboard.dao.BoardDao;
import com.cgkim.myboard.dao.CommentDao;
import com.cgkim.myboard.dao.MemberDao;
import com.cgkim.myboard.exception.BoardInsertFailedException;
import com.cgkim.myboard.exception.BoardNotFoundException;
import com.cgkim.myboard.exception.GuestPasswordInvalidException;
import com.cgkim.myboard.exception.GuestPasswordMismatchException;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.exception.LoginRequiredException;
import com.cgkim.myboard.exception.NoAuthorizationException;
import com.cgkim.myboard.util.AttachURIProvider;
import com.cgkim.myboard.util.SHA256PasswordEncoder;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import com.cgkim.myboard.vo.board.BoardUpdateRequest;
import com.cgkim.myboard.vo.board.BoardVo;
import com.cgkim.myboard.vo.common.GuestPasswordCheckRequest;
import com.cgkim.myboard.vo.common.GuestSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * 자유게시판 Service
 */
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardDao boardDao;

    private final CommentDao commentDao;

    private final MemberDao memberDao;

    private final BoardAttachDao boardAttachDao;

    private final SHA256PasswordEncoder sha256PasswordEncoder;

    private final AttachURIProvider attachURIProvider;


    /**
     * 게시물 목록 조회
     *
     * @param boardSearchRequest
     * @return List<BoardListResponse>
     */
    public List<BoardListResponse> getBoardListBy(BoardSearchRequest boardSearchRequest) {

        List<BoardListResponse> boardList = boardDao.selectList(boardSearchRequest);

        setThumbnailURIsOf(boardList);

        return boardList;
    }

    private void setThumbnailURIsOf(List<BoardListResponse> boardList) {

        for (BoardListResponse boardListResponse : boardList) {
            setThumbnailURIOf(boardListResponse);
        }
    }

    public void setThumbnailURIOf(BoardListResponse boardListResponse) {

        String thumbnailURI = boardListResponse.getThumbnailUri();

        if (thumbnailURI != null) {
            boardListResponse.setThumbnailUri(attachURIProvider.getFullURIOf(thumbnailURI));
        }
    }

    /**
     * 검색조건에 해당하는 게시물 갯수 리턴
     *
     * @param boardSearchRequest
     * @return int
     */
    public int getTotalCountBy(BoardSearchRequest boardSearchRequest) {

        return boardDao.selectCount(boardSearchRequest);
    }

    /**
     * 게시물 상세 조회
     *
     * @param boardId
     * @return BoardDetailResponse
     */
    public BoardDetailResponse viewBoardDetail(Long boardId) {

        boardDao.increaseViewCnt(boardId); //조회수 1 증가

        BoardDetailResponse boardDetailResponse = boardDao.selectOne(boardId); //게시글 select

        if (boardDetailResponse == null) {
            throw new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }

        List<AttachVo> boardAttachList = boardAttachDao.selectList(boardId); //첨부파일 목록

        attachURIProvider.setImageURIsOf(boardAttachList);

        boardDetailResponse.setAttachList(boardAttachList); //set 첨부파일 목록
        boardDetailResponse.setCommentList(commentDao.selectList(boardId)); //set 댓글 목록

        return boardDetailResponse;
    }

    /**
     * 익명 게시물 등록
     *
     * @param guestSaveRequest
     * @param boardSaveRequest
     * @param attachInsertList
     * @return Long
     */
    @Transactional(rollbackFor = Exception.class)
    public Long write(GuestSaveRequest guestSaveRequest,
                      BoardSaveRequest boardSaveRequest,
                      List<AttachVo> attachInsertList) throws NoSuchAlgorithmException {

        BoardVo boardVo = BoardVo.builder()
                .categoryId(boardSaveRequest.getCategoryId())
                .title(boardSaveRequest.getTitle())
                .content(boardSaveRequest.getContent())
                .guestNickname(guestSaveRequest.getGuestNickname())
                .guestPassword(sha256PasswordEncoder.getHash(guestSaveRequest.getGuestPassword()))
                .build();

        try {

            boardDao.insertGuestBoard(boardVo); //게시물 insert
            Long boardId = boardVo.getBoardId(); //auto increment 값 리턴

            if (isNotEmpty(attachInsertList)) {

                insertAttaches(attachInsertList, boardId);  //첨부파일 insert
                updateHasAttach(boardId); //첨부파일 유무 update
                updateThumbnailUri(attachInsertList, boardId); //썸네일 URI update
            }

            return boardId;

        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제

            e.printStackTrace();
            throw new BoardInsertFailedException(attachInsertList, ErrorCode.BOARD_INSERT_FAILED);
        }
    }

    private boolean isNotEmpty(List<AttachVo> attachList) {
        return attachList != null && !attachList.isEmpty();
    }

    /**
     * 회원 게시물 등록
     *
     * @param memberId
     * @param boardSaveRequest
     * @param attachInsertList
     * @return Long
     */
    @Transactional(rollbackFor = Exception.class)
    public Long write(Long memberId,
                      BoardSaveRequest boardSaveRequest,
                      List<AttachVo> attachInsertList) {

        BoardVo boardVo = BoardVo.builder()
                .categoryId(boardSaveRequest.getCategoryId())
                .title(boardSaveRequest.getTitle())
                .content(boardSaveRequest.getContent())
                .build();

        try {

            boardVo.setMemberId(memberId);
            boardDao.insertMemberBoard(boardVo);

            Long boardId = boardVo.getBoardId();

            if (isNotEmpty(attachInsertList)) {

                insertAttaches(attachInsertList, boardId); //첨부파일 insert
                updateHasAttach(boardId); //첨부파일 유무 update
                updateThumbnailUri(attachInsertList, boardId); //썸네일 URI update
            }

            return boardId;

        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제

            e.printStackTrace();
            throw new BoardInsertFailedException(attachInsertList, ErrorCode.BOARD_INSERT_FAILED);
        }
    }

    private void updateThumbnailUri(List<AttachVo> attachList, Long boardId) {

        for (AttachVo attach : attachList) {

            if (attach.isImage()) {

                String thumbnailUri = attachURIProvider.createThumbnailURIForDB(attach);
                boardDao.updateThumbnailUri(Map.of("boardId", boardId, "thumbnailUri", thumbnailUri));

                return;
            }
        }

        boardDao.updateThumbnailUri(Map.of("boardId", boardId, "thumbnailUri", ""));
    }

    /**
     * 게시물 수정
     *
     * @param boardId
     * @param boardUpdateRequest
     * @param attachInsertList
     * @param attachDeleteList
     */
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long boardId,
                       BoardUpdateRequest boardUpdateRequest,
                       List<AttachVo> attachInsertList,
                       List<AttachVo> attachDeleteList) {

        if (isNotEmpty(attachDeleteList)) { //첨부파일 delete
            deleteAttaches(attachDeleteList);
        }

        if (isNotEmpty(attachInsertList)) { //첨부파일 insert
            insertAttaches(attachInsertList, boardId);
        }

        boardUpdateRequest.setBoardId(boardId);

        boardDao.update(boardUpdateRequest);  //게시물 update
        updateHasAttach(boardId);  //첨부파일 유무 update
        updateThumbnailUri(boardAttachDao.selectList(boardId), boardId); //썸네일 URI 업데이트
    }

    /**
     * 게시물 소유권 인증
     *
     * @param boardId
     * @param username
     * @param guestPasswordCheckRequest
     * @throws NoSuchAlgorithmException
     */
    public void checkOwner(Long boardId, String username, GuestPasswordCheckRequest guestPasswordCheckRequest) throws NoSuchAlgorithmException {

        if (isAdminBoard(boardId)) { //관리자 글이면
            throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);

        } else if (isGuestBoard(boardId)) { //익명 글이면

            validateGuestPassword(guestPasswordCheckRequest); //비밀번호 유효성 검증
            checkGuestPassword(boardId, guestPasswordCheckRequest); //비밀번호 체크

        } else if (isMemberBoard(boardId)) { //회원 글이면

            if (username == null) {
                throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
            }

            Long targetMemberId = memberDao.selectMemberIdByUsername(username); //검증 대상 memberId

            if (targetMemberId == null) {
                throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
            }

            Long realMemberId = boardDao.selectMemberId(boardId); //실제 memberId

            if (!targetMemberId.equals(realMemberId)) { //검증
                throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
            }
        }
    }

    private boolean isGuestBoard(Long boardId) {
        return boardDao.selectGuestNickname(boardId) != null;
    }

    private boolean isMemberBoard(Long boardId) {
        return boardDao.selectMemberId(boardId) != null;
    }

    private boolean isAdminBoard(Long boardId) {
        return boardDao.selectAdminId(boardId) != null;
    }

    private void validateGuestPassword(GuestPasswordCheckRequest guestPasswordCheckRequest) {

        String guestPassword = guestPasswordCheckRequest.getGuestPassword();

        if (guestPassword == null || guestPassword.equals("")) {
            throw new GuestPasswordInvalidException(ErrorCode.GUEST_PASSWORD_INVALID);
        }
    }

    /**
     * 게시물 삭제
     *
     * @param boardId
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long boardId) {

        commentDao.deleteByBoardId(boardId);
        boardAttachDao.deleteByBoardId(boardId);
        boardDao.deleteByBoardId(boardId);
    }

    /**
     * 익명 게시물 비밀번호 체크
     *
     * @param boardId
     * @param guestPasswordCheckRequest
     * @throws NoSuchAlgorithmException
     */
    public void checkGuestPassword(Long boardId, GuestPasswordCheckRequest guestPasswordCheckRequest) throws NoSuchAlgorithmException {

        String guestPassword = guestPasswordCheckRequest.getGuestPassword();

        Long selectedBoardId = boardDao.selectBoardIdByGuestPassword(
                Map.of(
                        "boardId", boardId,
                        "guestPassword", sha256PasswordEncoder.getHash(guestPassword)
                )
        );

        if (selectedBoardId == null) {
            throw new GuestPasswordMismatchException(ErrorCode.GUEST_PASSWORD_MISMATCH);
        }
    }

    private void insertAttaches(List<AttachVo> attachInsertList, Long boardId) {

        for (AttachVo attach : attachInsertList) {

            attach.setBoardId(boardId);
            boardAttachDao.insert(attach);
        }
    }

    private void deleteAttaches(List<AttachVo> attachDeleteList) {

        for (AttachVo attachVo : attachDeleteList) {

            boardAttachDao.delete(attachVo.getAttachId());
        }
    }

    private void updateHasAttach(Long boardId) {

        int attachCount = boardAttachDao.selectCountByBoardId(boardId);
        boolean hasAttach = attachCount > 0;

        boardDao.updateHasAttach(
                Map.of(
                        "boardId", boardId,
                        "hasAttach", hasAttach
                )
        );
    }
}
