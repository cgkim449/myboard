package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.attach.AttachVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NoticeAttachDao {
    List<AttachVo> selectList(Long noticeId);
    AttachVo selectOne(Long attachId);
    int selectCountByNoticeId(long noticeId);
    int insert(AttachVo attach);
    void deleteByNoticeId(Long noticeId);
    void delete(Long attachId);

}