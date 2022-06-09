package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.AnswerDao;
import com.cgkim.myboard.dao.QuestionAttachDao;
import com.cgkim.myboard.dao.QuestionDao;
import com.cgkim.myboard.exception.BoardInsertFailedException;
import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.service.QuestionService;
import com.cgkim.myboard.util.SHA256PasswordEncoder;
import com.cgkim.myboard.vo.answer.AnswerVo;
import com.cgkim.myboard.vo.attach.AttachVo;
import com.cgkim.myboard.vo.question.QuestionDetailResponse;
import com.cgkim.myboard.vo.question.QuestionListResponse;
import com.cgkim.myboard.vo.question.QuestionSaveRequest;
import com.cgkim.myboard.vo.question.QuestionSearchRequest;
import com.cgkim.myboard.vo.question.QuestionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;
    private final AnswerDao answerDao;
    private final QuestionAttachDao questionAttachDao;

    @Override
    public List<QuestionListResponse> getQuestionList(QuestionSearchRequest questionSearchRequest) {
        List<QuestionListResponse> questionList = questionDao.selectList(questionSearchRequest);
        return questionList;
    }

    @Override
    public int getTotalCounts(QuestionSearchRequest questionSearchRequest) {
        return questionDao.selectCount(questionSearchRequest);
    }

    @Override
    public long write(Long memberId, QuestionSaveRequest questionSaveRequest, List<AttachVo> attachInsertList) {
        try {
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
        questionDetailResponse.setAttachList(questionAttachDao.selectList(id)); //첨부파일 리스트

        AnswerVo answerVo = answerDao.selectByQuestionId(id);
        questionDetailResponse.setAnswer(answerVo);
        return questionDetailResponse;
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
                String thumbnailUri = attach.getUploadPath() + File.separator + attach.getUuid() + "_200x200" + "." + attach.getExtension();
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
