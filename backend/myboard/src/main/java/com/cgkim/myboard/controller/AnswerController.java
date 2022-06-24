package com.cgkim.myboard.controller;

import com.cgkim.myboard.argumentresolver.LoginUser;
import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.AdminService;
import com.cgkim.myboard.service.AnswerService;
import com.cgkim.myboard.vo.answer.AnswerDetailResponse;
import com.cgkim.myboard.vo.answer.AnswerSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;


/**
 * Q&A 답변 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerService answerService;

    /**
     * 답변 상세 조회
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> getDetail(@PathVariable Long id) {

        AnswerDetailResponse answerDetail = answerService.viewDetail(id);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("answerDetail", answerDetail));
    }
}