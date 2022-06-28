package com.cgkim.myboard.service;

import com.cgkim.myboard.dao.AnswerAttachDao;
import com.cgkim.myboard.dao.AnswerDao;
import com.cgkim.myboard.dao.MemberDao;
import com.cgkim.myboard.dao.QuestionAttachDao;
import com.cgkim.myboard.dao.QuestionDao;
import com.cgkim.myboard.exception.LoginRequiredException;
import com.cgkim.myboard.exception.NoAuthorizationException;
import com.cgkim.myboard.exception.QuestionInsertFailedException;
import com.cgkim.myboard.exception.QuestionNotFoundException;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.util.AttachURIProvider;
import com.cgkim.myboard.vo.answer.AnswerDetailResponse;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.question.QuestionDetailResponse;
import com.cgkim.myboard.vo.question.QuestionListResponse;
import com.cgkim.myboard.vo.question.QuestionSaveRequest;
import com.cgkim.myboard.vo.question.QuestionSearchRequest;
import com.cgkim.myboard.vo.question.QuestionUpdateRequest;
import com.cgkim.myboard.vo.question.QuestionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Q&A 질문 Service
 */
@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionDao questionDao;

    private final AnswerDao answerDao;

    private final MemberDao memberDao;

    private final QuestionAttachDao questionAttachDao;

    private final AnswerAttachDao answerAttachDao;

    private final AttachURIProvider attachURIProvider;

    /**
     * 질문 목록 조회
     *
     * @param questionSearchRequest
     * @return List<QuestionListResponse>
     */
    public List<QuestionListResponse> getQuestionList(QuestionSearchRequest questionSearchRequest) {

        List<QuestionListResponse> questionList = questionDao.selectList(questionSearchRequest);

        setThumbnailURIsOf(questionList);

        return questionList;
    }

    private void setThumbnailURIsOf(List<QuestionListResponse> questionList) {

        for (QuestionListResponse questionListResponse : questionList) {
            setThumbnailURIOf(questionListResponse);
        }
    }

    public void setThumbnailURIOf(QuestionListResponse questionListResponse) {

        String thumbnailURI = questionListResponse.getThumbnailUri();

        if (thumbnailURI != null) {
            questionListResponse.setThumbnailUri(attachURIProvider.getFullURIOf(thumbnailURI));
        }
    }

    /**
     * 검색조건에 해당하는 질문 갯수 리턴
     *
     * @param questionSearchRequest
     * @return int
     */
    public int getTotalCount(QuestionSearchRequest questionSearchRequest) {
        return questionDao.selectCount(questionSearchRequest);
    }

    /**
     * 질문 상세조회
     *
     * @param questionId
     * @return QuestionDetailResponse
     */
    //TODO: 리팩토링
    public QuestionDetailResponse viewQuestionDetail(Long questionId) {

        questionDao.increaseViewCnt(questionId);

        QuestionDetailResponse questionDetailResponse = questionDao.selectOne(questionId);

        if (questionDetailResponse == null) {
            throw new QuestionNotFoundException(ErrorCode.QUESTION_NOT_FOUND);
        }

        List<AttachVo> questionAttachList = questionAttachDao.selectListByQuestionId(questionId);

        attachURIProvider.setImageURIsOf(questionAttachList);

        questionDetailResponse.setAttachList(questionAttachList); //첨부파일 리스트
        AnswerDetailResponse answerDetailResponse = answerDao.selectByQuestionId(questionId);

        if (answerDetailResponse != null) { //질문에 달린 답변이 있으면

            List<AttachVo> answerAttachList = answerAttachDao.selectList(answerDetailResponse.getAnswerId());

            attachURIProvider.setImageURIsOf(answerAttachList);

            answerDetailResponse.setAttachList(answerAttachList);
            questionDetailResponse.setAnswer(answerDetailResponse);
        }

        return questionDetailResponse;
    }

    /**
     * 질문 작성
     *
     * @param memberId
     * @param questionSaveRequest
     * @param attachInsertList
     * @return Long
     */
    public Long write(Long memberId, QuestionSaveRequest questionSaveRequest, List<AttachVo> attachInsertList) {

        QuestionVo questionVo = QuestionVo.builder()
                .categoryId(questionSaveRequest.getCategoryId())
                .title(questionSaveRequest.getTitle())
                .content(questionSaveRequest.getContent())
                .memberId(memberId)
                .isSecret(questionSaveRequest.getIsSecret())
                .build();

        try {

            questionDao.insert(questionVo);
            Long questionId = questionVo.getQuestionId();

            if (isNotEmpty(attachInsertList)) {

                insertAttaches(attachInsertList, questionId);
                updateHasAttach(questionId);
                updateThumbnailUri(attachInsertList, questionId);
            }

            return questionId;

        } catch (Exception e) {
            throw new QuestionInsertFailedException(attachInsertList, ErrorCode.QUESTION_INSERT_FAILED);
        }
    }

    private boolean isNotEmpty(List<AttachVo> attachList) {

        return attachList != null && !attachList.isEmpty();
    }


    /**
     * 질문 삭제
     *
     * @param questionId
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long questionId) {

        questionAttachDao.deleteByQuestionId(questionId);
        questionDao.delete(questionId);
    }

    /**
     * 질문 소유권 인증
     *
     * @param questionId
     * @param username
     */
    public void checkOwner(Long questionId, String username) {

        if (isAdminQuestion(questionId)) {
            throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);

        } else if (isMemberQuestion(questionId)) {

            if (username == null) {
                throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
            }

            Long targetMemberId = memberDao.selectMemberIdByUsername(username);

            if (targetMemberId == null) {
                throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
            }

            Long realMemberId = questionDao.selectMemberId(questionId);

            if (!targetMemberId.equals(realMemberId)) {
                throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
            }
        }
    }

    private boolean isMemberQuestion(Long questionId) {
        return questionDao.selectMemberId(questionId) != null;
    }

    private boolean isAdminQuestion(Long questionId) {
        return questionDao.selectAdminId(questionId) != null;
    }

    /**
     * 답변 유무 검증
     *
     * @param questionId
     */
    public void checkHasAnswer(Long questionId) {

        Long answerId = answerDao.selectAnswerIdByQuestionId(questionId);

        if (answerId != null) {
            throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
        }
    }

    /**
     * 질문 수정
     *
     * @param questionId
     * @param questionUpdateRequest
     * @param attachInsertList
     * @param attachDeleteList
     */
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long questionId,
                       QuestionUpdateRequest questionUpdateRequest,
                       List<AttachVo> attachInsertList,
                       List<AttachVo> attachDeleteList
    ) {

        if (attachDeleteList != null && attachDeleteList.size() > 0) { //첨부파일 delete
            deleteAttaches(attachDeleteList);
        }

        if (attachInsertList != null && attachInsertList.size() > 0) { //첨부파일 insert
            insertAttaches(attachInsertList, questionId);
        }

        questionUpdateRequest.setQuestionId(questionId);

        questionDao.update(questionUpdateRequest);
        updateHasAttach(questionId);
        updateThumbnailUri(questionAttachDao.selectListByQuestionId(questionId), questionId);
    }

    private void deleteAttaches(List<AttachVo> attachDeleteList) {

        for (AttachVo attachVo : attachDeleteList) {

            questionAttachDao.deleteByAttachId(attachVo.getAttachId());
        }
    }

    private void insertAttaches(List<AttachVo> attachInsertList, Long id) {

        for (AttachVo attach : attachInsertList) {

            attach.setQuestionId(id);
            questionAttachDao.insert(attach);
        }
    }

    private void updateThumbnailUri(List<AttachVo> attachList, Long questionId) {

        for (AttachVo attach : attachList) {

            if (attach.isImage()) {

                String thumbnailUri = attachURIProvider.createThumbnailURIForDB(attach);
                questionDao.updateThumbnailUri(Map.of("questionId", questionId, "thumbnailUri", thumbnailUri));

                return;
            }
        }

        questionDao.updateThumbnailUri(Map.of("questionId", questionId, "thumbnailUri", ""));
    }

    private void updateHasAttach(Long questionId) {

        int attachCount = questionAttachDao.selectCountByQuestionId(questionId);
        boolean hasAttach = attachCount > 0;

        questionDao.updateHasAttach(
                Map.of(
                        "questionId", questionId,
                        "hasAttach", hasAttach
                )
        );
    }
}