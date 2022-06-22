package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.comment.CommentListResponse;
import com.cgkim.myboardadmin.vo.comment.CommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface CommentDao {
    List<CommentListResponse> selectList(Long boardId);
    void deleteByBoardId(Long boardId);

    void deleteByCommentId(Long commentId);

    Long selectMemberId(Long commentId);

    Long selectOneByGuestPassword(Map<String,Object> map);

    Long insertComment(CommentVo commentVo);
}