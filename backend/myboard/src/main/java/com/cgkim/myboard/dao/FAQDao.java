package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.faq.FAQListResponse;
import com.cgkim.myboard.vo.question.QuestionDetailResponse;
import com.cgkim.myboard.vo.question.QuestionListResponse;
import com.cgkim.myboard.vo.question.QuestionSearchRequest;
import com.cgkim.myboard.vo.question.QuestionVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface FAQDao {
    List<FAQListResponse> selectList(Integer categoryId);
}
