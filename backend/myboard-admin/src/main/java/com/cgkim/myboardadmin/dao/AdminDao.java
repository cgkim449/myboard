package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.admin.AdminVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AdminDao {

    AdminVo selectByUsername(String username);

    Long selectAdminIdByUsername(String username);
}
