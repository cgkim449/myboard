package com.cgkim.myboard.controller;

import com.cgkim.myboard.argumentresolver.LoginUser;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.exception.LoginRequiredException;
import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.service.impl.BoardAttachServiceImpl;
import com.cgkim.myboard.util.FileHandler;
import com.cgkim.myboard.validator.BoardSaveRequestValidator;
import com.cgkim.myboard.validator.BoardUpdateRequestValidator;
import com.cgkim.myboard.validator.FileSaveRequestValidator;
import com.cgkim.myboard.validator.GuestSaveRequestValidator;
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
import org.hibernate.validator.constraints.ru.INN;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 자유게시판 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final BoardAttachServiceImpl attachService;
    private final FileHandler fileHandler;

    /**
     * 게시물 목록 조회
     * @param boardSearchRequest
     * @return ResponseEntity<SuccessResponse>
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getBoardList(BoardSearchRequest boardSearchRequest){

        List<BoardListResponse> boardList = boardService.getBoardList(boardSearchRequest);
        int boardTotalCount = boardService.getTotalCount(boardSearchRequest);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("boardList", boardList)
                        .put("boardTotalCount", boardTotalCount));
    }

    /**
     * 게시물 상세 조회
     * @param boardId
     * @return ResponseEntity<SuccessResponse>
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> getBoardDetail(@PathVariable Long boardId) {

        BoardDetailResponse boardDetail = boardService.viewBoardDetail(boardId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("boardDetail", boardDetail));
    }

    /**
     * 회원 게시물 작성
     * @param username
     * @param boardSaveRequest
     * @param fileSaveRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException
     */
    @PostMapping("/member")
    public ResponseEntity<SuccessResponse> writeBoard(@LoginUser String username,
                                                      @Valid BoardSaveRequest boardSaveRequest,
                                                      @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {

        if(username == null) {
            throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
        }

        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //첨부파일 생성 (C://upload)
        long boardId = boardService.write(username, boardSaveRequest, attachInsertList); //글 작성

        return ResponseEntity.created(URI.create("/boards/" + boardId)).body(new SuccessResponse());
    }

    /**
     * 익명 게시물 작성
     * @param guestSaveRequest
     * @param boardSaveRequest
     * @param fileSaveRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException
     */
    @PostMapping("/guest")
    public ResponseEntity<SuccessResponse> writeBoard(@Valid GuestSaveRequest guestSaveRequest,
                                                      @Valid BoardSaveRequest boardSaveRequest,
                                                      @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {

        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //첨부파일 생성 (C://upload)
        long boardId = boardService.write(guestSaveRequest, boardSaveRequest, attachInsertList); //익명 글 작성

        return ResponseEntity.created(URI.create("/boards/" + boardId)).body(new SuccessResponse());
    }

    /**
     * 게시물 수정
     * @param username
     * @param guestPassword
     * @param boardId
     * @param boardUpdateRequest
     * @param fileSaveRequest
     * @param attachDeleteRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    //TODO: 게시물 수정시 썸네일 업데이트 해야함.
    @PatchMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> updateBoard(@LoginUser String username,
                                                       @RequestParam(required = false) String guestPassword,
                                                       @PathVariable Long boardId,
                                                       @Valid BoardUpdateRequest boardUpdateRequest,
                                                       @Valid FileSaveRequest fileSaveRequest,
                                                       Long[] attachDeleteRequest
    ) throws IOException, NoSuchAlgorithmException {
        //게시글 소유권 인증.
        boardService.checkOwner(boardId, username, guestPassword);

        List<AttachVo> attachDeleteList = attachService.getList(attachDeleteRequest); //첨부파일 삭제 리스트
        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());//첨부파일 삽입 리스트

        boardService.modify( //게시글 수정, 첨부파일 수정
                boardId,
                boardUpdateRequest.getContent(),
                boardUpdateRequest.getTitle(),
                attachInsertList,
                attachDeleteList
        );

        fileHandler.deleteFiles(attachDeleteList); //첨부파일 삭제 (C://upload)

        return ResponseEntity.ok(new SuccessResponse());
    }

    /**
     * 게시물 삭제
     * @param username
     * @param guestPasswordCheckRequest
     * @param boardId
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> deleteBoard(@LoginUser String username,
                                                       @RequestBody(required = false) GuestPasswordCheckRequest guestPasswordCheckRequest,
                                                       @PathVariable Long boardId
    ) throws NoSuchAlgorithmException {

        //TODO: 리팩토링
        //TODO: 댓글 삭제도 손봐야될듯
        String guestPassword = null;
        if(guestPasswordCheckRequest != null) {
            guestPassword = guestPasswordCheckRequest.getGuestPassword();
        }

        boardService.checkOwner(boardId, username, guestPassword);

        List<AttachVo> attachDeleteList = attachService.getList(boardId); //첨부파일 삭제 리스트
        boardService.delete(boardId); //게시물 삭제
        fileHandler.deleteFiles(attachDeleteList); //첨부파일 삭제 (C://upload)

        return ResponseEntity.noContent().build();
    }

    /**
     * 익명 글 비밀번호 체크
     * @param username
     * @param guestPasswordCheckRequest
     * @param boardId
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/{boardId}/password-check")
    public ResponseEntity<SuccessResponse> checkGuestPassword(@LoginUser String username,
                                                              @RequestBody GuestPasswordCheckRequest guestPasswordCheckRequest,
                                                              @PathVariable Long boardId
    ) throws NoSuchAlgorithmException {

        //TODO: 리팩토링
        String guestPassword = null;
        if(guestPasswordCheckRequest != null) {
            guestPassword = guestPasswordCheckRequest.getGuestPassword();
        }

        //TODO: username 필요없음.
        boardService.checkOwner(boardId, username, guestPassword);

        return ResponseEntity.ok(new SuccessResponse());
    }
}