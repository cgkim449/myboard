package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.cgkim.myboard.vo.comment.CommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface CommentDao {
    List<CommentListResponse> selectList(Long boardId);
    void deleteByBoardId(Long boardId);

    void insertMemberComment(CommentVo build);

    void insertGuestComment(CommentVo build);

    void deleteByCommentId(Long commentId);

    Long selectMemberId(Long commentId);

    Long selectOneByGuestPassword(Map<String,Object> map);
}