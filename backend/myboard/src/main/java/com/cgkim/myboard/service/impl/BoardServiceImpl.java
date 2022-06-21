package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.BoardAttachDao;
import com.cgkim.myboard.dao.BoardDao;
import com.cgkim.myboard.dao.CommentDao;
import com.cgkim.myboard.dao.MemberDao;
import com.cgkim.myboard.exception.BoardInsertFailedException;
import com.cgkim.myboard.exception.GuestPasswordInvalidException;
import com.cgkim.myboard.exception.GuestPasswordMismatchException;
import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.exception.LoginRequiredException;
import com.cgkim.myboard.exception.NoAuthorizationException;
import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.util.SHA256PasswordEncoder;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import com.cgkim.myboard.vo.board.BoardVo;
import com.cgkim.myboard.vo.member.GuestSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Validated
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardDao boardDao;
    private final CommentDao commentDao;
    private final MemberDao memberDao;
    private final BoardAttachDao boardAttachDao;
    private final SHA256PasswordEncoder sha256PasswordEncoder;

    @Value("${host.url}")
    private String hostUrl;

    /**
     * 검색조건에 해당하는 게시물 리스트
     */
    @Override
    public List<BoardListResponse> getBoardList(BoardSearchRequest boardSearchRequest) {
        List<BoardListResponse> boardList = boardDao.selectList(boardSearchRequest);
        //TODO: 리팩토링
        for (BoardListResponse boardListResponse : boardList) {
            String thumbnailUri = boardListResponse.getThumbnailUri();
            if(thumbnailUri != null) {
                boardListResponse.setThumbnailUri(hostUrl + thumbnailUri);
            }
        }

        return boardList;
    }

    /**
     * 검색조건에 해당하는 게시물 총 갯수
     */
    @Override
    public int getTotalCount(BoardSearchRequest boardSearchRequest) {
        return boardDao.selectCount(boardSearchRequest);
    }

    /**
     * 게시물 상세 보기
     */
    @Override
    public BoardDetailResponse viewBoardDetail(Long boardId) {
        boardDao.increaseViewCnt(boardId); //조회수 1 증가

        BoardDetailResponse boardDetailResponse = boardDao.selectOne(boardId); //게시글

        //TODO: 리팩토링
        List<AttachVo> attachVoList = boardAttachDao.selectList(boardId);
        for (AttachVo attachVo : attachVoList) {
            if(attachVo.isImage()) {
                attachVo.setThumbnailUri(
                        hostUrl
                        + "upload"
                        + File.separator
                        + attachVo.getUploadPath()
                        + File.separator
                        + attachVo.getUuid()
                        + "_thumbnail"
                        + "."
                        +attachVo.getExtension());

                attachVo.setOriginalImageUri(
                        hostUrl
                        + "upload"
                        + File.separator
                        + attachVo.getUploadPath()
                        + File.separator
                        + attachVo.getUuid()
                        + "."
                        + attachVo.getExtension());
            }
        }

        boardDetailResponse.setAttachList(attachVoList); //첨부파일 리스트

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
                    .title(boardSaveRequest.getTitle())
                    .content(boardSaveRequest.getContent())
                    .guestNickname(guestSaveRequest.getGuestNickname())
                    .guestPassword(sha256PasswordEncoder.getHash(guestSaveRequest.getGuestPassword()))
                    .build();

            boardDao.insertGuestBoard(boardVo); //게시물 insert
            long boardId = boardVo.getBoardId();

            if (attachInsertList != null && !attachInsertList.isEmpty()) {
                insertAttaches(attachInsertList, boardId);  //첨부파일 insert
                updateHasAttach(boardId);
                updateThumbnailUri(attachInsertList, boardId);
            }

            return boardId; //등록한 게시물 번호 리턴
        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제하기 위해
            e.printStackTrace();
            throw new BoardInsertFailedException(attachInsertList, ErrorCode.BOARD_INSERT_FAILED);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long write(String username, BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList) {
        BoardVo boardVo = BoardVo.builder()
                .categoryId(boardSaveRequest.getCategoryId())
                .title(boardSaveRequest.getTitle())
                .content(boardSaveRequest.getContent())
                .build();
        try {
            long memberId = memberDao.selectMemberIdByUsername(username);
            boardVo.setMemberId(memberId);
            boardDao.insertMemberBoard(boardVo);

//            if(isAdmin) {
//                long adminId = adminDao.selectAdminIdByUsername(username);
//                boardVo.setAdminId(adminId);
//                boardDao.insertAdminBoard(boardVo);
//            } else {
//            }

            long boardId = boardVo.getBoardId();

            if (attachInsertList != null && !attachInsertList.isEmpty()) {
                insertAttaches(attachInsertList, boardId); //첨부파일 insert
                updateHasAttach(boardId); //첨부파일 유무 update
                updateThumbnailUri(attachInsertList, boardId);
            }

            return boardId; //등록한 게시물 번호 리턴
        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제하기 위해
            e.printStackTrace();
            throw new BoardInsertFailedException(attachInsertList, ErrorCode.BOARD_INSERT_FAILED);
        }
    }


    /**
     * 로그인 사용자 게시물 등록
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public long write(Long memberId, BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList) {
        try {
            BoardVo boardVo = BoardVo.builder()
                    .categoryId(boardSaveRequest.getCategoryId())
                    .title(boardSaveRequest.getTitle())
                    .content(boardSaveRequest.getContent())
                    .memberId(memberId)
                    .build();

            boardDao.insertMemberBoard(boardVo); //게시물 insert

            long boardId = boardVo.getBoardId();

            if (attachInsertList != null && !attachInsertList.isEmpty()) {
                insertAttaches(attachInsertList, boardId); //첨부파일 insert
                updateHasAttach(boardId); //첨부파일 유무 update
                updateThumbnailUri(attachInsertList, boardId);
            }

            return boardId; //등록한 게시물 번호 리턴
        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제하기 위해
            throw new BoardInsertFailedException(attachInsertList, ErrorCode.BOARD_INSERT_FAILED);
        }
    }



    /**
     * 게시물 썸네일 uri 업데이트
     */
    private boolean updateThumbnailUri(List<AttachVo> attachVoList, Long boardId) {
        for (AttachVo attach : attachVoList) {
            if(attach.isImage()) {
                String thumbnailUri = attach.getUploadPath() + File.separator + attach.getUuid() + "_thumbnail" + "." + attach.getExtension();
                boardDao.updateThumbnailUri(Map.of("boardId", boardId, "thumbnailUri", thumbnailUri));
                return true;
            }
        }
        return false;
    }

    /**
     * 게시물 수정
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(
            Long boardId,
            String content,
            String title,
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
                        "title", title,
                        "content", content
                )
        );  //게시물 update
        updateHasAttach(boardId);  //첨부파일 유무 update
        updateThumbnailUri(boardAttachDao.selectList(boardId), boardId);
    }

    @Override
    public boolean isAnonymous(Long boardId) {
        return boardDao.selectMemberId(boardId) == null;
    }

    @Override
    public void checkOwner(Long boardId, String username, String guestPassword) throws NoSuchAlgorithmException {
        //TODO: 관리자 글 체크 추가해야함
        if(isAnonymous(boardId)) { //익명 글이면

            validateGuestPassword(guestPassword); //비밀번호 유효성 검증
            checkGuestPassword(boardId, guestPassword); //비밀번호 체크
        } else { //회원 글이면

            if(username == null) {
                throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
            }

            Long verificationTargetMemberId = memberDao.selectMemberIdByUsername(username);

            if(verificationTargetMemberId == null) {
                throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
            }

            Long realMemberId = boardDao.selectMemberId(boardId);

            if(!verificationTargetMemberId.equals(realMemberId)) {
                throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
            }
        }
    }

    private void validateGuestPassword(String guestPassword) {
        if(guestPassword == null || guestPassword.equals("")) {
            throw new GuestPasswordInvalidException(ErrorCode.GUEST_PASSWORD_INVALID);
        }
    }

    /**
     * 게시물 삭제
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long boardId) {
        commentDao.deleteByBoardId(boardId);
        boardAttachDao.deleteByBoardId(boardId);
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
            boardAttachDao.insert(attach);
        }
    }

    /**
     * 첨부파일 delete
     */
    private void deleteAttaches(List<AttachVo> attachDeleteList) {
        for (AttachVo attachVo : attachDeleteList) {
            boardAttachDao.delete(attachVo.getAttachId());
        }
    }

    /**
     * 첨부파일 유무 여부 update
     */
    private void updateHasAttach(long boardId) {
        int attachCount = boardAttachDao.selectCountByBoardId(boardId);

        boardDao.updateHasAttach(
                Map.of(
                        "boardId", boardId,
                        "hasAttach", attachCount > 0
                )
        );
    }

}
