package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.faq.FAQListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FAQDao {
    List<FAQListResponse> selectList(Integer categoryId);
}
