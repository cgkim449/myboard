package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.faq.FAQListResponse;
import com.cgkim.myboardadmin.vo.faq.FAQVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface FAQDao {
    List<FAQListResponse> selectList(Integer categoryId);

    void insertFAQ(FAQVo faqVo);

    void updateHasAttach(Map<String, Object> updateHasAttachMap);

    void updateThumbnailUri(Map<String,Object> updateThumbnailUriMap);
}
