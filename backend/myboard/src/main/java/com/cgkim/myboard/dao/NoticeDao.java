package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.notice.NoticeDetailResponse;
import com.cgkim.myboard.vo.notice.NoticeListResponse;
import com.cgkim.myboard.vo.notice.NoticeSearchRequest;
import com.cgkim.myboard.vo.notice.NoticeVo;
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
     * 가장 최근 공지 조회
     *
     * @return NoticeDetailResponse
     */
    NoticeDetailResponse selectLatestOne();
}
