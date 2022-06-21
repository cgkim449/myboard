package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.AdminDao;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.exception.LoginFailedException;
import com.cgkim.myboard.service.AdminService;
import com.cgkim.myboard.util.SHA256PasswordEncoder;
import com.cgkim.myboard.vo.admin.AdminVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminDao adminDao;
    private final SHA256PasswordEncoder sha256PasswordEncoder;


    /**
     * 로그인
     *
     * @return null: 로그인 실패
     */
    @Override
    public AdminVo login(String username, String password) throws NoSuchAlgorithmException {
        AdminVo adminVo = adminDao.selectByUsername(username);

        if(adminVo == null) { //일치하는 아이디가 없을때
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }

        password = sha256PasswordEncoder.getHash(password);
        if(!adminVo.getPassword().equals(password)) { //비밀번호가 틀렸을 때
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }

        return adminVo;
    }

    @Override
    public Long getAdminId(String username) {
        return adminDao.selectAdminIdByUsername(username);
    }
}
