package com.cgkim.myboard.controller;

import com.cgkim.myboard.argumentresolver.LoginAdmin;
import com.cgkim.myboard.response.SuccessResponse;
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
import java.io.IOException;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<SuccessResponse> write(
            @LoginAdmin Long adminId,
            @Valid AnswerSaveRequest answerSaveRequest
    ) {
        long id = answerService.write(adminId, answerSaveRequest);
        return ResponseEntity.created(URI.create("/answers/" + id)).body(new SuccessResponse());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> getDetail(@PathVariable Long id) {
        AnswerDetailResponse answerDetail = answerService.viewDetail(id);
        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("answerDetail", answerDetail));
    }
}