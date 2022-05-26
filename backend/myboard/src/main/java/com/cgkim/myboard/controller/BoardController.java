package com.cgkim.myboard.controller;

import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.util.FileHandler;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.attach.FileSaveRequest;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
}