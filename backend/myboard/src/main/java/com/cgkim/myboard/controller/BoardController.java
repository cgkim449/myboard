package com.cgkim.myboard.controller;

import com.cgkim.myboard.argumentresolver.LoginMember;
import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.exception.GuestPasswordInvalidException;
import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.AttachService;
import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.util.FileHandler;
import com.cgkim.myboard.validation.BoardSaveRequestValidator;
import com.cgkim.myboard.validation.BoardUpdateRequestValidator;
import com.cgkim.myboard.validation.FileSaveRequestValidator;
import com.cgkim.myboard.validation.GuestSaveRequestValidator;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.attach.FileSaveRequest;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import com.cgkim.myboard.vo.board.BoardUpdateRequest;
import com.cgkim.myboard.vo.member.GuestPasswordCheckRequest;
import com.cgkim.myboard.vo.member.GuestSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
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

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final AttachService attachService;
    private final FileHandler fileHandler;
    private final BoardSaveRequestValidator boardSaveRequestValidator;
    private final FileSaveRequestValidator fileSaveRequestValidator;
    private final BoardUpdateRequestValidator boardUpdateRequestValidator;
    private final GuestSaveRequestValidator guestSaveRequestValidator;

    /**
     * PropertyEditor, Validator 등록
     */
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        addPropertyEditors(webDataBinder);
        addValidators(webDataBinder);
    }

    /**
     * PropertyEditor 등록
     */
    private void addPropertyEditors(WebDataBinder webDataBinder) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
    }

    /**
     * Validator 등록
     */
    private void addValidators(WebDataBinder webDataBinder) {
        if (webDataBinder.getTarget() == null) {
            return;
        }
        final List<Validator> validatorList = List.of(
                boardSaveRequestValidator,
                boardUpdateRequestValidator,
                fileSaveRequestValidator,
                guestSaveRequestValidator
        );
        for (Validator validator : validatorList) {
            if (validator.supports(webDataBinder.getTarget().getClass())) {
                webDataBinder.addValidators(validator);
            }
        }
    }

    /**
     * 게시물 목록 API
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getBoardList(BoardSearchRequest boardSearchRequest){
        List<BoardListResponse> boardList = boardService.getBoardList(boardSearchRequest);
        int boardTotalCounts = boardService.getTotalCounts(boardSearchRequest);
        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("boardList", boardList)
                        .put("boardTotalCounts", boardTotalCounts));
    }

    /**
     * 게시물 상세 보기 API
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> getBoardDetail(@PathVariable Long boardId) {
        BoardDetailResponse boardDetail = boardService.viewBoardDetail(boardId);
        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("boardDetail", boardDetail));
    }

    /**
     * 회원 게시물 등록 API
     */
    @PostMapping("/member")
    public ResponseEntity<SuccessResponse> writeMemberBoard(
            @LoginMember Long memberId,
            @Valid BoardSaveRequest boardSaveRequest,
            @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {
        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //첨부파일 생성 (C://upload)
        long boardId = boardService.write(memberId, boardSaveRequest, attachInsertList); //회원 글 작성
        return ResponseEntity.created(URI.create("/boards/" + boardId)).body(new SuccessResponse());
    }

    /**
     * 익명 게시물 등록 API
     */
    @PostMapping("/guest")
    public ResponseEntity<SuccessResponse> writeGuestBoard(
            @Valid GuestSaveRequest guestSaveRequest,
            @Valid BoardSaveRequest boardSaveRequest,
            @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {
        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //첨부파일 생성 (C://upload)
        long boardId = boardService.write(guestSaveRequest, boardSaveRequest, attachInsertList); //익명 글 작성
        return ResponseEntity.created(URI.create("/boards/" + boardId)).body(new SuccessResponse());
    }

    /**
     * 게시물 수정 API
     */
    @PatchMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> updateBoard(
            @PathVariable Long boardId,
            GuestPasswordCheckRequest guestPasswordCheckRequest,
            @Valid BoardUpdateRequest boardUpdateRequest,
            @Valid FileSaveRequest fileSaveRequest,
            Long[] attachDeleteRequest
    ) throws IOException, NoSuchAlgorithmException {
        if(boardService.isAnonymous(boardId)) { //익명 글일때만
            validateGuestPassword(guestPasswordCheckRequest.getGuestPassword()); //비밀번호 유효성 검증
            boardService.checkGuestPassword(boardId, guestPasswordCheckRequest.getGuestPassword()); //비밀번호 체크
        }

        List<AttachVo> attachDeleteList = attachService.getList(attachDeleteRequest); //첨부파일 삭제 리스트
        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());//첨부파일 삽입 리스트
        boardService.modify( //게시글 수정, 첨부파일 수정
                boardId,
                boardUpdateRequest.getBoardContent(),
                boardUpdateRequest.getBoardTitle(),
                attachInsertList,
                attachDeleteList
        );
        fileHandler.deleteFiles(attachDeleteList); //첨부파일 삭제 (C://upload)
        return ResponseEntity.ok(new SuccessResponse());
    }

    /**
     * 게시물 삭제 API
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> deleteBoard(
            @PathVariable Long boardId,
            @RequestBody GuestPasswordCheckRequest guestPasswordCheckRequest
    ) throws NoSuchAlgorithmException {
        if(boardService.isAnonymous(boardId)) { //익명 글일때만
            validateGuestPassword(guestPasswordCheckRequest.getGuestPassword()); //비밀번호 유효성 검증
            boardService.checkGuestPassword(boardId, guestPasswordCheckRequest.getGuestPassword()); //비밀번호 체크
        }

        List<AttachVo> attachDeleteList = attachService.getList(boardId); //첨부파일 삭제 리스트
        boardService.delete(boardId); //게시물 삭제
        fileHandler.deleteFiles(attachDeleteList); //첨부파일 삭제 (C://upload)
        return ResponseEntity.noContent().build();
    }

    /**
     * 익명 글 비밀번호 체크 API
     */
    @PostMapping("/{boardId}/pwCheck")
    public ResponseEntity<SuccessResponse> checkGuestPassword(
            @PathVariable Long boardId,
            @RequestBody GuestPasswordCheckRequest guestPasswordCheckRequest
    ) throws NoSuchAlgorithmException {
        if(boardService.isAnonymous(boardId)) { //익명 글일때만
            validateGuestPassword(guestPasswordCheckRequest.getGuestPassword()); //비밀번호 유효성 검증
            boardService.checkGuestPassword(boardId, guestPasswordCheckRequest.getGuestPassword()); //비밀번호 체크
        }

        return ResponseEntity.ok(new SuccessResponse());
    }

    private void validateGuestPassword(String guestPassword) {
        if(guestPassword == null || guestPassword.equals("")) {
            throw new GuestPasswordInvalidException(ErrorCode.GUEST_PASSWORD_INVALID);
        }
    }
}