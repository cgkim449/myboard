package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.comment.CommentListResponse;
import com.cgkim.myboard.vo.comment.CommentSaveRequest;
import com.cgkim.myboard.vo.comment.CommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CommentDao {
    List<CommentListResponse> selectList(Long boardId);
    void deleteByBoardId(Long boardId);

    void insertUserComment(CommentVo build);

    void insertGuestComment(CommentVo build);
}