package com.cgkim.myboardadmin.service;



import com.cgkim.myboardadmin.vo.admin.AdminVo;

import java.security.NoSuchAlgorithmException;

public interface AdminService {
    AdminVo login(String username, String password) throws NoSuchAlgorithmException;

    Long getAdminId(String username);
}
