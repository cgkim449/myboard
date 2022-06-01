package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.user.SignUpRequest;
import com.cgkim.myboard.vo.user.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDao {

    void insert(SignUpRequest signUpRequest);
    UserVo selectByUserId(Long userId);
    UserVo selectByUsername(String username);
    int selectCountByUsername(String username);
    int selectCountByNickname(String nickname);

    Long selectUserIdByUsername(String username);
}
