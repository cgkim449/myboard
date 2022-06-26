package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.faq.FAQListResponse;
import com.cgkim.myboardadmin.vo.faq.FAQVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * FAQ DAO
 */
@Mapper
@Repository
public interface FAQDao {

    /**
     * 카테고리에 해당하는 FAQ 조회
     *
     * @param categoryId
     * @return List<FAQListResponse>
     */
    List<FAQListResponse> selectList(Integer categoryId);

    /**
     * FAQ 삽입
     *
     * @param faqVo
     */
    void insertFAQ(FAQVo faqVo);

    /**
     * 첨부파일 유무 업데이트
     *
     * @param updateHasAttachMap
     */
    void updateHasAttach(Map<String, Object> updateHasAttachMap);

    /**
     * 썸네일 uri 업데이트
     *
     * @param updateThumbnailUriMap
     */
    void updateThumbnailUri(Map<String,Object> updateThumbnailUriMap);
}
