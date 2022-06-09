package com.cgkim.myboard.service;


import com.cgkim.myboard.vo.admin.AdminVo;

import java.security.NoSuchAlgorithmException;

public interface AdminService {
    AdminVo login(String username, String password) throws NoSuchAlgorithmException;

    Object getAdminId(String username);
}
