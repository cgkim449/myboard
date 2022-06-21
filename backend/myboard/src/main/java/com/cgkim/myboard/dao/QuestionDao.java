package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.board.BoardDetailResponse;
import com.cgkim.myboard.vo.board.BoardListResponse;
import com.cgkim.myboard.vo.board.BoardSearchRequest;
import com.cgkim.myboard.vo.board.BoardVo;
import com.cgkim.myboard.vo.question.QuestionDetailResponse;
import com.cgkim.myboard.vo.question.QuestionListResponse;
import com.cgkim.myboard.vo.question.QuestionSearchRequest;
import com.cgkim.myboard.vo.question.QuestionVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface QuestionDao {
    List<QuestionListResponse> selectList(QuestionSearchRequest questionSearchRequest);
    int selectCount(QuestionSearchRequest questionSearchRequest);

    void insert(QuestionVo questionVo);
    int updateThumbnailUri(Map<String,Object> map);
    void updateHasAttach(Map<String, Object> map);

    void increaseViewCnt(Long id);

    QuestionDetailResponse selectOne(Long id);

    void delete(Long questionId);

    Long selectMemberId(Long questionId);

    int update(Map<String, Object> questionId);

//    Long selectMemberId(Long boardId);
}
