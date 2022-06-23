package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.board.BoardDetailResponse;
import com.cgkim.myboardadmin.vo.board.BoardListResponse;
import com.cgkim.myboardadmin.vo.board.BoardSearchRequest;
import com.cgkim.myboardadmin.vo.board.BoardVo;
import com.cgkim.myboardadmin.vo.notice.NoticeDetailResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeListResponse;
import com.cgkim.myboardadmin.vo.notice.NoticeSearchRequest;
import com.cgkim.myboardadmin.vo.notice.NoticeVo;
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
