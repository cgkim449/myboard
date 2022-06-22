package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.notice.NoticeDetailResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface NoticeDao {
    NoticeDetailResponse selectLatestOne();
}
