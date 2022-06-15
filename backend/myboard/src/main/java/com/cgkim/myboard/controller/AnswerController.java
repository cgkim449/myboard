package com.cgkim.myboard.controller;

import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.AdminService;
import com.cgkim.myboard.service.AnswerService;
import com.cgkim.myboard.vo.answer.AnswerDetailResponse;
import com.cgkim.myboard.vo.answer.AnswerSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Setter
    private String username;
    private final AnswerService answerService;
    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<SuccessResponse> write(
            @Valid AnswerSaveRequest answerSaveRequest
    ) {
        Long adminId = adminService.getAdminId(username);
        Long answerId = answerService.write(adminId, answerSaveRequest);
        return ResponseEntity.created(URI.create("/answers/" + answerId)).body(new SuccessResponse());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> getDetail(@PathVariable Long id) {
        AnswerDetailResponse answerDetail = answerService.viewDetail(id);
        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("answerDetail", answerDetail));
    }
}