package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.question.QuestionDetailResponse;
import com.cgkim.myboard.vo.question.QuestionListResponse;
import com.cgkim.myboard.vo.question.QuestionSearchRequest;
import com.cgkim.myboard.vo.question.QuestionUpdateRequest;
import com.cgkim.myboard.vo.question.QuestionVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 질문 DAO
 */
@Mapper
@Repository
public interface QuestionDao {

    /**
     * 질문 목록 조회
     *
     * @param questionSearchRequest
     * @return List<QuestionListResponse>
     */
    List<QuestionListResponse> selectList(QuestionSearchRequest questionSearchRequest);

    /**
     * 질문 갯수 조회
     *
     * @param questionSearchRequest
     * @return int
     */
    int selectCount(QuestionSearchRequest questionSearchRequest);

    /**
     * 질문 삽입
     *
     * @param questionVo
     */
    void insert(QuestionVo questionVo);

    /**
     * 썸네일 uri 업데이트
     *
     * @param updateThumbnailUriMap
     * @return int
     */
    int updateThumbnailUri(Map<String, Object> updateThumbnailUriMap);

    /**
     * 첨부파일 유무 업데이트
     *
     * @param updateHasAttachMap
     */
    void updateHasAttach(Map<String, Object> updateHasAttachMap);

    /**
     * 조회수 증가
     *
     * @param questionId
     */
    void increaseViewCnt(Long questionId);

    /**
     * 질문 조회
     *
     * @param questionId
     * @return QuestionDetailResponse
     */
    QuestionDetailResponse selectOne(Long questionId);

    /**
     * 질문 삭제
     *
     * @param questionId
     */
    void delete(Long questionId);

    /**
     * 질문 작성자(회원) ID 조회
     *
     * @param questionId
     * @return Long
     */
    Long selectMemberId(Long questionId);

    /**
     * 질문 작성자(관리자) ID 조회
     *
     * @param questionId
     * @return Long
     */
    Long selectAdminId(Long questionId);

    /**
     * 질문 업데이트
     *
     * @param questionUpdateRequest
     * @return int
     */
    int update(QuestionUpdateRequest questionUpdateRequest);
}
