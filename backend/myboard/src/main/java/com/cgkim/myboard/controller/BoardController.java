package com.cgkim.myboard.controller;

import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.AttachService;
import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.util.FileHandler;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.attach.FileSaveRequest;
import com.cgkim.myboard.vo.board.BoardDeleteRequest;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardPwCheckRequest;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import com.cgkim.myboard.vo.board.BoardUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final AttachService attachService;
    private final FileHandler fileHandler;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        addPropertyEditors(webDataBinder);
    }

    private void addPropertyEditors(WebDataBinder webDataBinder) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
    }

    /**
     * 게시물 목록
     *
     * @param boardSearchRequest
     * @return
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getBoardList(BoardSearchRequest boardSearchRequest){
        return ResponseEntity.ok()
                .body(new SuccessResponse()
                        .put("boardList", boardService.getBoardList(boardSearchRequest))
                        .put("boardTotalCounts", boardService.getTotalCounts(boardSearchRequest)));
    }

    /**
     * 게시물 상세보기
     *
     * @param boardId
     * @return
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> getBoardDetail(@PathVariable Long boardId) {
        return ResponseEntity.ok()
                .body(new SuccessResponse()
                        .put("board", boardService.viewBoardDetail(boardId)));
    }

    /**
     * 게시물 등록
     *
     * @param boardSaveRequest
     * @param fileSaveRequest
     * @return
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> writeBoard(
            BoardSaveRequest boardSaveRequest,
            FileSaveRequest fileSaveRequest
    ) throws IOException {
        //TODO: validation
        List<AttachVo> attachInsertList = fileHandler.saveFiles(fileSaveRequest.getMultipartFiles()); // 파일 생성 (C://upload)
        long boardId = boardService.write(boardSaveRequest, attachInsertList); // 게시물, 파일 insert (DB)

        return ResponseEntity.ok()
                .body(new SuccessResponse()
                        .put("boardId", boardId));
    }

    /**
     * 게시물 삭제
     *
     * @param boardDeleteRequest
     * @return
     */

    @DeleteMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> deleteBoard(@RequestBody BoardDeleteRequest boardDeleteRequest) {

        boardService.pwCheck(boardDeleteRequest.getBoardId(), boardDeleteRequest.getGuestPassword()); // 비밀번호 체크
        List<AttachVo> attachDeleteList = attachService.getList(boardDeleteRequest.getBoardId()); // 삭제할 파일 리스트
        boardService.delete(boardDeleteRequest.getBoardId()); // 게시물, 댓글, 파일 삭제(db)
        fileHandler.deleteFiles(attachDeleteList); // 파일 삭제(C://upload)

        return ResponseEntity.ok()
                .body(new SuccessResponse());
    }


    /**
     * 게시물 수정
     *
     * @param boardUpdateRequest
     * @param fileSaveRequest
     * @param attachDeleteRequest
     * @return
     * @throws IOException
     */
    @PatchMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> updateBoard(
            BoardUpdateRequest boardUpdateRequest,
            FileSaveRequest fileSaveRequest,
            Long[] attachDeleteRequest
    ) throws IOException {
        //TODO: validation
        boardService.pwCheck(boardUpdateRequest.getBoardId(), boardUpdateRequest.getGuestPassword()); // 비밀번호 체크
        List<AttachVo> attachDeleteList = attachService.getList(attachDeleteRequest); // 삭제할 첨부파일 리스트
        List<AttachVo> attachInsertList = fileHandler.saveFiles(fileSaveRequest.getMultipartFiles()); // 새로 추가한 파일 생성
        boardService.modify(boardUpdateRequest, attachInsertList, attachDeleteList); // 게시물 수정 및 파일 삭제(DB)
        fileHandler.deleteFiles(attachDeleteList); // 파일 삭제

        return ResponseEntity.ok()
                .body(new SuccessResponse());
    }

    /**
     * 게시물 비밀번호 확인
     *
     * @param boardPwCheckRequest
     * @return
     */
    @PostMapping("/{boardId}/pwCheck")
    public ResponseEntity<SuccessResponse> pwCheck(@RequestBody BoardPwCheckRequest boardPwCheckRequest) {
        boardService.pwCheck(boardPwCheckRequest);
        return ResponseEntity.ok()
                .body(new SuccessResponse());
    }
}