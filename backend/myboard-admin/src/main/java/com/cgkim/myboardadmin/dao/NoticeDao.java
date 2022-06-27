package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.notice.NoticeDetailResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeListResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeSearchRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeUpdateRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 공지 DAO
 */
@Mapper
@Repository
public interface NoticeDao {

    /**
     * 공지 목록 조회
     *
     * @param noticeSearchRequest
     * @return List<NoticeListResponse>
     */
    List<NoticeListResponse> selectList(NoticeSearchRequest noticeSearchRequest);

    /**
     * 공지 목록 총 갯수 조회
     *
     * @param noticeSearchRequest
     * @return int
     */
    int selectCount(NoticeSearchRequest noticeSearchRequest);

    /**
     * 공지 상세 조회
     *
     * @param noticeId
     * @return NoticeDetailResponse
     */
    NoticeDetailResponse selectOne(Long noticeId);

    /**
     * 첨부파일 유무 업데이트
     *
     * @param updateHasAttachMap
     */
    void updateHasAttach(Map<String, Object> updateHasAttachMap);

    /**
     * 공지 삭제
     *
     * @param noticeId
     */
    void delete(Long noticeId);

    /**
     * 공지 업데이트
     *
     * @param noticeUpdateRequest
     * @return int
     */
    int update(NoticeUpdateRequest noticeUpdateRequest);

    /**
     * 공지 삽입
     *
     * @param noticeVo
     */
    void insertNotice(NoticeVo noticeVo);

    /**
     * 썸네일 uri 업데이트
     *
     * @param updateThumbnailUriMap
     * @return int
     */
    int updateThumbnailUri(Map<String,Object> updateThumbnailUriMap);

    /**
     * 가장 최근 공지 조회
     *
     * @return NoticeDetailResponse
     */
    NoticeDetailResponse selectLatestOne();
}
