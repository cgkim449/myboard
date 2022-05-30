package com.cgkim.myboard.controller;

import com.cgkim.myboard.service.AttachService;
import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.util.FileHandler;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.attach.FileSaveRequest;
import com.cgkim.myboard.vo.board.BoardDeleteRequest;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
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

    private final AttachService attachService;
    private final BoardService boardService;
    private final FileHandler fileHandler;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        addPropertyEditors(webDataBinder);
    }

    private void addPropertyEditors(WebDataBinder webDataBinder) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
    }

    @GetMapping
    public ResponseEntity<?> getBoardList(BoardSearchRequest boardSearchRequest){

        List<BoardListResponse> boardListResponse = boardService.getBoardList(boardSearchRequest);
        int boardTotalCounts = boardService.getTotalCounts(boardSearchRequest);

        return ResponseEntity.ok().body(Map.of("boardList", boardListResponse, "boardTotalCounts", boardTotalCounts));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoardDetail(@PathVariable Long boardId) {
        BoardDetailResponse boardDetailResponse = boardService.viewBoardDetail(boardId);

        return ResponseEntity.ok().body(Map.of("boardDetail", boardDetailResponse));
    }

    //TODO: 서버에서 유효성 검증
    @PostMapping
    public ResponseEntity<?> writeBoard(BoardSaveRequest boardSaveRequest,
                                        FileSaveRequest fileSaveRequest) throws IOException {
        List<AttachVo> attachInsertList = fileHandler.saveFiles(fileSaveRequest.getMultipartFiles()); // 파일 생성 (C://upload)
        long boardId = boardService.write(boardSaveRequest, attachInsertList); // 게시물, 파일 insert (DB)

        return ResponseEntity.ok()
                .body(Map.of("boardId", boardId));
    }

    @PostMapping("/{boardId}/pwCheck")
    public ResponseEntity<?> pwCheck(@RequestBody BoardPwCheckRequest boardPwCheckRequest) {
        Long result = boardService.pwCheck(boardPwCheckRequest);
        if(result == null) {
            return ResponseEntity.badRequest().body("비밀번호 틀림");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@RequestBody BoardDeleteRequest boardDeleteRequest) {
        System.out.println(boardDeleteRequest);
        Long result = boardService.pwCheck(boardDeleteRequest.getBoardId(), boardDeleteRequest.getGuestPassword()); // 비밀번호 체크
        if(result == null) {
            return ResponseEntity.badRequest().body("비밀번호 틀림");
        }
        List<AttachVo> attachDeleteList = attachService.getList(boardDeleteRequest.getBoardId()); // 삭제할 파일 리스트
        boardService.delete(boardDeleteRequest.getBoardId()); // 게시물, 댓글, 파일 삭제(db)
        fileHandler.deleteFiles(attachDeleteList); // 파일 삭제(C://upload)

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{boardNo}")
    public ResponseEntity<?> updateBoard(BoardUpdateRequest boardUpdateRequest,
                                         FileSaveRequest fileSaveRequest,
                                         Long[] attachDeleteRequest) throws IOException {

        Long result = boardService.pwCheck(boardUpdateRequest.getBoardId(), boardUpdateRequest.getGuestPassword()); // 비밀번호 체크
        if(result == null) {
            return ResponseEntity.badRequest().body("비밀번호 틀림");
        }
        List<AttachVo> attachDeleteList = attachService.getList(attachDeleteRequest); // 삭제할 첨부파일 리스트
        List<AttachVo> attachInsertList = fileHandler.saveFiles(fileSaveRequest.getMultipartFiles()); // 새로 추가한 파일 생성
        boardService.modify(boardUpdateRequest, attachInsertList, attachDeleteList); // 게시물 수정 및 파일 삭제(DB)
        fileHandler.deleteFiles(attachDeleteList); // 파일 삭제

        return ResponseEntity.ok().build();
    }
}