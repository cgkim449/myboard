package com.cgkim.myboard.controller;

import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.vo.BoardVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<?> showBoardList(){
        List<BoardVo> boardVoList = boardService.showBoardList();
        return ResponseEntity.ok().body(boardVoList);
    }
}