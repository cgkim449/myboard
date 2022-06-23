package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.attach.AttachVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FAQAttachDao {
    List<AttachVo> selectList(Long faqId);
    AttachVo selectOne(Long attachId);
    int selectCountByFAQId(long faqId);
    int insert(AttachVo attach);
    void deleteByFAQId(Long boardId);
    void delete(Long attachId);

}