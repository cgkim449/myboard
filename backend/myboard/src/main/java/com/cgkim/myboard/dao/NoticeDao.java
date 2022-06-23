package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.notice.NoticeDetailResponse;
import com.cgkim.myboard.vo.notice.NoticeListResponse;
import com.cgkim.myboard.vo.notice.NoticeSearchRequest;
import com.cgkim.myboard.vo.notice.NoticeVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface NoticeDao {
    List<NoticeListResponse> selectList(NoticeSearchRequest noticeSearchRequest);
    int selectCount(NoticeSearchRequest noticeSearchRequest);
    NoticeDetailResponse selectOne(Long noticeId);
    void updateHasAttach(Map<String, Object> updateHasAttachMap);
    void delete(Long noticeId);
    int update(Map<String, Object> map);
    void insertNotice(NoticeVo noticeVo);
    int updateThumbnailUri(Map<String,Object> updateThumbnailUriMap);

    NoticeDetailResponse selectLatestOne();
}
