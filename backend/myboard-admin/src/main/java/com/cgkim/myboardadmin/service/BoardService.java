package com.cgkim.myboardadmin.service;

import com.cgkim.myboardadmin.dao.AdminDao;
import com.cgkim.myboardadmin.dao.BoardAttachDao;
import com.cgkim.myboardadmin.dao.BoardDao;
import com.cgkim.myboardadmin.dao.CommentDao;
import com.cgkim.myboardadmin.exception.BoardInsertFailedException;
import com.cgkim.myboardadmin.exception.BoardNotFoundException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.util.AttachURIProvider;
import com.cgkim.myboardadmin.vo.attach.AttachVo;
import com.cgkim.myboardadmin.vo.board.BoardDetailResponse;
import com.cgkim.myboardadmin.vo.board.BoardListResponse;
import com.cgkim.myboardadmin.vo.board.BoardSaveRequest;
import com.cgkim.myboardadmin.vo.board.BoardSearchRequest;
import com.cgkim.myboardadmin.vo.board.BoardUpdateRequest;
import com.cgkim.myboardadmin.vo.board.BoardVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

/**
 * 자유게시판 Service
 */
@RequiredArgsConstructor
@Validated
@Service
public class BoardService {

    private final BoardDao boardDao;

    private final CommentDao commentDao;

    private final AdminDao adminDao;

    private final BoardAttachDao boardAttachDao;

    private final AttachURIProvider attachURIProvider;


    /**
     * 게시물 목록 조회
     *
     * @param boardSearchRequest
     * @return List<BoardListResponse>
     */
    public List<BoardListResponse> getBoardList(BoardSearchRequest boardSearchRequest) {

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
    public int getTotalCount(BoardSearchRequest boardSearchRequest) {

        return boardDao.selectCount(boardSearchRequest);
    }

    /**
     * 게시물 상세 조회
     *
     * @param boardId
     * @return BoardDetailResponse
     */
    public BoardDetailResponse viewBoardDetail(Long boardId) {

        boardDao.increaseViewCnt(boardId);

        BoardDetailResponse BoardDetailResponse = boardDao.selectOne(boardId);

        if(BoardDetailResponse == null) {
            throw new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }

        List<AttachVo> boardAttachList = boardAttachDao.selectList(boardId);

        attachURIProvider.setImageURIsOf(boardAttachList);

        BoardDetailResponse.setAttachList(boardAttachList);
        BoardDetailResponse.setCommentList(commentDao.selectList(boardId));

        return BoardDetailResponse;
    }

    /**
     * 게시물 등록
     *
     * @param username
     * @param boardSaveRequest
     * @param attachInsertList
     * @return Long
     */
    @Transactional(rollbackFor = Exception.class)
    public Long write(String username, BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList) {

        BoardVo boardVo = BoardVo.builder()
                .categoryId(boardSaveRequest.getCategoryId())
                .title(boardSaveRequest.getTitle())
                .content(boardSaveRequest.getContent())
                .build();

        try {

            Long adminId = adminDao.selectAdminIdByUsername(username);
            boardVo.setAdminId(adminId);
            boardDao.insertBoard(boardVo);

            Long boardId = boardVo.getBoardId();

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
                       List<AttachVo> attachDeleteList
    ) {

        if(isNotEmpty(attachDeleteList)) {

            deleteAttaches(attachDeleteList);
        }

        if(isNotEmpty(attachInsertList)) {

            insertAttaches(attachInsertList, boardId);
        }

        boardUpdateRequest.setBoardId(boardId);

        boardDao.update(boardUpdateRequest);
        updateHasAttach(boardId);
        updateThumbnailUri(boardAttachDao.selectList(boardId), boardId);
    }

    private boolean isNotEmpty(List<AttachVo> attachList) {

        return attachList != null && !attachList.isEmpty();
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
        boardDao.delete(boardId);
    }
}
