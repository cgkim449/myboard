package com.cgkim.myboardadmin.service.impl;

import com.cgkim.myboardadmin.dao.AdminDao;
import com.cgkim.myboardadmin.dao.BoardAttachDao;
import com.cgkim.myboardadmin.dao.BoardDao;
import com.cgkim.myboardadmin.dao.CommentDao;
import com.cgkim.myboardadmin.dao.MemberDao;
import com.cgkim.myboardadmin.exception.BoardInsertFailedException;
import com.cgkim.myboardadmin.exception.BoardNotFoundException;
import com.cgkim.myboardadmin.exception.GuestPasswordInvalidException;
import com.cgkim.myboardadmin.exception.GuestPasswordMismatchException;
import com.cgkim.myboardadmin.exception.LoginRequiredException;
import com.cgkim.myboardadmin.exception.NoAuthorizationException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.service.BoardService;
import com.cgkim.myboardadmin.util.SHA256PasswordEncoder;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.board.BoardDetailResponse;
import com.cgkim.myboardadmin.vo.board.BoardListResponse;
import com.cgkim.myboardadmin.vo.board.BoardSaveRequest;
import com.cgkim.myboardadmin.vo.board.BoardSearchRequest;
import com.cgkim.myboardadmin.vo.board.BoardVo;
import com.cgkim.myboardadmin.vo.member.GuestSaveRequest;
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
    private final AdminDao adminDao;
    private final BoardAttachDao boardAttachDao;

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
                boardListResponse.setThumbnailUri(hostUrl + "upload" + File.separator + thumbnailUri);
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

        if(boardDetailResponse == null) {
            throw new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }

        //TODO: 리팩토링
        List<AttachVo> attachVoList = boardAttachDao.selectList(boardId);

        for (AttachVo attachVo : attachVoList) {
            if(attachVo.isImage()) {
                setImageUriOf(attachVo);
            }
        }

        boardDetailResponse.setAttachList(attachVoList); //첨부파일 리스트

        boardDetailResponse.setCommentList(commentDao.selectList(boardId)); //댓글 리스트

        return boardDetailResponse;
    }

    private void setImageUriOf(AttachVo attachVo) {
        attachVo.setThumbnailUri(makeThumbnailUriOf(attachVo));
        attachVo.setOriginalImageUri(makeOriginalImageUriOf(attachVo));
    }

    private String makeOriginalImageUriOf(AttachVo attachVo) {
        return hostUrl +
                "upload" +
                File.separator +
                attachVo.getUploadPath() +
                File.separator +
                attachVo.getUuid() +
                "." +
                attachVo.getExtension();
    }

    private String makeThumbnailUriOf(AttachVo attachVo) {
        return hostUrl +
                "upload" +
                File.separator +
                attachVo.getUploadPath() +
                File.separator +
                attachVo.getUuid() +
                "_thumbnail" +
                "." +
                attachVo.getExtension();
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
            long adminId = adminDao.selectAdminIdByUsername(username);
            boardVo.setAdminId(adminId);
            boardDao.insertBoard(boardVo);

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

    /**
     * 게시물 수정
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long boardId,
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
}
