package com.cgkim.myboard.controller;

import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        addPropertyEditors(webDataBinder);
    }

    private void addPropertyEditors(WebDataBinder webDataBinder) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
    }

    @GetMapping
    public ResponseEntity<?> getBoardList(BoardSearchRequest request){

        List<BoardListResponse> boardListResponse = boardService.getBoardList(request);
        int boardTotalCounts = boardService.getTotalCounts(request);

        return ResponseEntity.ok().body(Map.of("boardList", boardListResponse, "boardTotalCounts", boardTotalCounts));
    }
}