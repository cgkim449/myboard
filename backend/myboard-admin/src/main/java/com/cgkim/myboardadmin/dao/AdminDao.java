package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.admin.AdminVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 관리자 DAO
 */
@Repository
@Mapper
public interface AdminDao {

    /**
     * username 으로 AdminVo select
     *
     * @param username
     * @return AdminVo
     */
    AdminVo selectByUsername(String username);

    /**
     * username 으로 AdminId select
     *
     * @param username
     * @return Long
     */
    Long selectAdminIdByUsername(String username);
}
