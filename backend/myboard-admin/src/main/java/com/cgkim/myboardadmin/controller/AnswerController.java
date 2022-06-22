package com.cgkim.myboardadmin.controller;

import com.cgkim.myboardadmin.argumentresolver.LoginUser;
import com.cgkim.myboardadmin.response.SuccessResponse;
import com.cgkim.myboardadmin.service.AdminService;
import com.cgkim.myboardadmin.service.AnswerService;
import com.cgkim.myboardadmin.vo.answer.AnswerDetailResponse;
import com.cgkim.myboardadmin.vo.answer.AnswerSaveRequest;
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


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerService answerService;
    private final AdminService adminService;

    //TODO: 관리자 프로젝트로 이동
    @PostMapping
    public ResponseEntity<SuccessResponse> write(@LoginUser String username,
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