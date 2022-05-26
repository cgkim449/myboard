package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.User.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDao {

    void insert(UserVo userVo);
}
