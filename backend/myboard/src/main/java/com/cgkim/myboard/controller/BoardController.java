package com.cgkim.myboard.controller;

import com.cgkim.myboard.argumentresolver.LoginUser;
import com.cgkim.myboard.exception.MemberNotFoundException;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.exception.LoginRequiredException;
import com.cgkim.myboard.response.SuccessResponse;
import com.cgkim.myboard.service.BoardAttachService;
import com.cgkim.myboard.service.BoardService;
import com.cgkim.myboard.service.MemberService;
import com.cgkim.myboard.util.FileHandler;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.common.FileSaveRequest;
import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSaveRequest;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import com.cgkim.myboard.vo.board.BoardUpdateRequest;
import com.cgkim.myboard.vo.common.GuestPasswordCheckRequest;
import com.cgkim.myboard.vo.common.GuestSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final BoardAttachService attachService;

    private final MemberService memberService;

    private final FileHandler fileHandler;

    /**
     * 게시물 목록 조회
     *
     * @param boardSearchRequest
     * @return ResponseEntity<SuccessResponse>
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getBoardList(BoardSearchRequest boardSearchRequest) {

        List<BoardListResponse> boardList = boardService.getBoardListBy(boardSearchRequest);
        int boardTotalCount = boardService.getTotalCountBy(boardSearchRequest);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("boardList", boardList)
                        .put("boardTotalCount", boardTotalCount));
    }

    /**
     * 게시물 상세 조회
     *
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
     *
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

        if (username == null) {
            throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
        }

        Long memberId = memberService.getMemberIdBy(username);

        if (memberId == null) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //물리적 파일 생성
        Long boardId = boardService.write(memberId, boardSaveRequest, attachInsertList); //글 작성

        return ResponseEntity.created(URI.create("/boards/" + boardId)).body(new SuccessResponse());
    }

    /**
     * 익명 게시물 작성
     *
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
    ) throws IOException, NoSuchAlgorithmException {

        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //물리적 파일 생성
        long boardId = boardService.write(guestSaveRequest, boardSaveRequest, attachInsertList); //글 작성

        return ResponseEntity.created(URI.create("/boards/" + boardId)).body(new SuccessResponse());
    }

    /**
     * 게시물 수정
     *
     * @param username
     * @param boardId
     * @param boardUpdateRequest
     * @param fileSaveRequest
     * @param guestPasswordCheckRequest
     * @param attachDeleteRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @PatchMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> updateBoard(@PathVariable Long boardId,
                                                       @LoginUser String username,
                                                       @Valid BoardUpdateRequest boardUpdateRequest,
                                                       @Valid FileSaveRequest fileSaveRequest,
                                                       GuestPasswordCheckRequest guestPasswordCheckRequest,
                                                       Long[] attachDeleteRequest
    ) throws IOException, NoSuchAlgorithmException {

        boardService.checkOwner(boardId, username, guestPasswordCheckRequest); //게시글 소유권 인증

        List<AttachVo> attachDeleteList = attachService.getList(attachDeleteRequest); //첨부파일 삭제 리스트
        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());//첨부파일 삽입 리스트

        boardService.modify(boardId, boardUpdateRequest, attachInsertList, attachDeleteList); //게시글, 첨부파일 DB 업데이트

        fileHandler.deleteFiles(attachDeleteList); //물리적 파일 삭제

        return ResponseEntity.ok(new SuccessResponse());
    }

    /**
     * 게시물 삭제
     *
     * @param username
     * @param guestPasswordCheckRequest
     * @param boardId
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> deleteBoard(@PathVariable Long boardId,
                                                       @LoginUser String username,
                                                       @RequestBody(required = false) GuestPasswordCheckRequest guestPasswordCheckRequest
    ) throws NoSuchAlgorithmException {

        boardService.checkOwner(boardId, username, guestPasswordCheckRequest);

        List<AttachVo> attachDeleteList = attachService.getList(boardId); //첨부파일 삭제 리스트
        boardService.delete(boardId); //게시물 삭제
        fileHandler.deleteFiles(attachDeleteList); //물리적 파일 삭제

        return ResponseEntity.noContent().build();
    }

    /**
     * 익명 글 비밀번호 확인
     *
     * @param guestPasswordCheckRequest
     * @param boardId
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/{boardId}/password-check")
    public ResponseEntity<SuccessResponse> checkGuestPassword(@PathVariable Long boardId,
                                                              @RequestBody GuestPasswordCheckRequest guestPasswordCheckRequest
    ) throws NoSuchAlgorithmException {

        boardService.checkGuestPassword(boardId, guestPasswordCheckRequest);

        return ResponseEntity.ok(new SuccessResponse());
    }
}