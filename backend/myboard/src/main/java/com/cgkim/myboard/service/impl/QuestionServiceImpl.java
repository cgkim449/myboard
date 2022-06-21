package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.AnswerDao;
import com.cgkim.myboard.dao.MemberDao;
import com.cgkim.myboard.dao.QuestionAttachDao;
import com.cgkim.myboard.dao.QuestionDao;
import com.cgkim.myboard.exception.BoardInsertFailedException;
import com.cgkim.myboard.exception.BoardNotFoundException;
import com.cgkim.myboard.exception.LoginRequiredException;
import com.cgkim.myboard.exception.NoAuthorizationException;
import com.cgkim.myboard.exception.QuestionNotFoundException;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.service.QuestionService;
import com.cgkim.myboard.vo.answer.AnswerVo;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.question.QuestionDetailResponse;
import com.cgkim.myboard.vo.question.QuestionListResponse;
import com.cgkim.myboard.vo.question.QuestionSaveRequest;
import com.cgkim.myboard.vo.question.QuestionSearchRequest;
import com.cgkim.myboard.vo.question.QuestionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;
    private final AnswerDao answerDao;
    private final MemberDao memberDao;
    private final QuestionAttachDao questionAttachDao;

    @Value("${host.url}")
    private String hostUrl;

    @Override
    public List<QuestionListResponse> getQuestionList(QuestionSearchRequest questionSearchRequest) {
        List<QuestionListResponse> questionList = questionDao.selectList(questionSearchRequest);

        //TODO: 리팩토링
        for (QuestionListResponse questionListResponse : questionList) {
            String thumbnailUri = questionListResponse.getThumbnailUri();
            if(thumbnailUri != null) {
                questionListResponse.setThumbnailUri(hostUrl + "upload" + File.separator + thumbnailUri);
            }
        }

        return questionList;
    }

    @Override
    public int getTotalCount(QuestionSearchRequest questionSearchRequest) {
        return questionDao.selectCount(questionSearchRequest);
    }

    @Override
    public long write(String username, QuestionSaveRequest questionSaveRequest, List<AttachVo> attachInsertList) {
        try {
            long memberId = memberDao.selectMemberIdByUsername(username);

            QuestionVo questionVo = QuestionVo.builder()
                    .categoryId(questionSaveRequest.getCategoryId())
                    .title(questionSaveRequest.getTitle())
                    .content(questionSaveRequest.getContent())
                    .memberId(memberId)
                    .isSecret(questionSaveRequest.getIsSecret())
                    .build();

            questionDao.insert(questionVo);//질문 insert
            long id = questionVo.getQuestionId();

            if (attachInsertList != null && !attachInsertList.isEmpty()) {
                insertAttaches(attachInsertList, id); //첨부파일 insert
                updateHasAttach(id); //첨부파일 유무 update
                updateThumbnailUri(attachInsertList, id);
            }

            return id; //등록한 게시물 번호 리턴
        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제하기 위해 TODO: question
            throw new BoardInsertFailedException(attachInsertList, ErrorCode.BOARD_INSERT_FAILED);
        }
    }

    @Override
    public QuestionDetailResponse viewDetail(Long id) {
        questionDao.increaseViewCnt(id); //조회수 1 증가

        QuestionDetailResponse questionDetailResponse = questionDao.selectOne(id); //질문

        if(questionDetailResponse == null) {
            throw new QuestionNotFoundException(ErrorCode.QUESTION_NOT_FOUND);
        }

        //TODO: 리팩토링
        List<AttachVo> attachVoList = questionAttachDao.selectList(id);

        for (AttachVo attachVo : attachVoList) {
            if (attachVo.isImage()) {
                attachVo.setThumbnailUri(
                        hostUrl
                                + "upload"
                                + File.separator
                                + attachVo.getUploadPath()
                                + File.separator
                                + attachVo.getUuid()
                                + "_thumbnail"
                                + "."
                                + attachVo.getExtension());

                attachVo.setOriginalImageUri(
                        hostUrl
                                + "upload"
                                + File.separator
                                + attachVo.getUploadPath()
                                + File.separator
                                + attachVo.getUuid()
                                + "."
                                + attachVo.getExtension());
            }
        }
        questionDetailResponse.setAttachList(attachVoList); //첨부파일 리스트

        AnswerVo answerVo = answerDao.selectByQuestionId(id);
        questionDetailResponse.setAnswer(answerVo);
        return questionDetailResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long questionId) {
        questionAttachDao.deleteById(questionId);
        questionDao.delete(questionId);
    }

    @Override
    public void checkOwner(Long questionId, String username) {
        if(username == null) {
            throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
        }

        Long verificationTargetMemberId = memberDao.selectMemberIdByUsername(username);

        if(verificationTargetMemberId == null) {
            throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
        }

        Long realMemberId = questionDao.selectMemberId(questionId);

        if(!verificationTargetMemberId.equals(realMemberId)) {
            throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
        }
    }

    @Override
    public void checkHasAnswer(Long questionId) {
        Long answerId = answerDao.selectAnswerIdByQuestionId(questionId);

        if(answerId != null) {
            throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
        }
    }

    /**
     * 질문 수정
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long questionId,
                       String content,
                       String title,
                       Integer isSecret,
                       List<AttachVo> attachInsertList,
                       List<AttachVo> attachDeleteList
    ) {

        if(attachDeleteList != null && attachDeleteList.size() > 0) { //첨부파일 delete
            deleteAttaches(attachDeleteList);
        }

        if(attachInsertList != null && attachInsertList.size() > 0) { //첨부파일 insert
            insertAttaches(attachInsertList, questionId);
        }

        questionDao.update(
                Map.of(
                        "questionId", questionId,
                        "title", title,
                        "content", content,
                        "isSecret", isSecret
                )
        );

        updateHasAttach(questionId);  //첨부파일 유무 update
        updateThumbnailUri(questionAttachDao.selectList(questionId), questionId);
    }

    /**
     * 첨부파일 delete
     */
    private void deleteAttaches(List<AttachVo> attachDeleteList) {
        for (AttachVo attachVo : attachDeleteList) {
            questionAttachDao.delete(attachVo.getAttachId());
        }
    }

    /**
     * 첨부파일 insert
     */
    private void insertAttaches(List<AttachVo> attachInsertList, Long id) {
        for (AttachVo attach : attachInsertList) {
            attach.setQuestionId(id);
            questionAttachDao.insert(attach);
        }
    }

    /**
     * 썸네일 uri 업데이트
     */
    private boolean updateThumbnailUri(List<AttachVo> attachVoList, Long id) {
        for (AttachVo attach : attachVoList) {
            if(attach.isImage()) {
                String thumbnailUri = attach.getUploadPath() + File.separator + attach.getUuid() + "_thumbnail" + "." + attach.getExtension();
                questionDao.updateThumbnailUri(Map.of("id", id, "thumbnailUri", thumbnailUri));
                return true;
            }
        }
        questionDao.updateThumbnailUri(Map.of("id", id, "thumbnailUri", ""));
        return false;
    }

    /**
     * 첨부파일 유무 여부 update
     */
    private void updateHasAttach(long id) {
        int attachCount = questionAttachDao.selectCountById(id);

        questionDao.updateHasAttach(
                Map.of(
                        "id", id,
                        "hasAttach", attachCount > 0
                )
        );
    }
}
