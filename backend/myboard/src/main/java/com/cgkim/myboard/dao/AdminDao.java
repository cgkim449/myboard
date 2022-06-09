package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.admin.AdminVo;
import com.cgkim.myboard.vo.member.MemberVo;
import com.cgkim.myboard.vo.member.SignUpRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AdminDao {

    AdminVo selectByUsername(String username);

    Long selectAdminIdByUsername(String username);
}
