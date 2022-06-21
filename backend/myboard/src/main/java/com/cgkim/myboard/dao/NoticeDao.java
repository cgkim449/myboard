package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.faq.FAQListResponse;
import com.cgkim.myboard.vo.notice.NoticeDetailResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NoticeDao {
    NoticeDetailResponse selectLatestOne();
}
